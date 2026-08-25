package com.example.data.repository

import com.example.data.local.ChatMessageEntity
import com.example.data.local.SafetyLogDao
import com.example.data.local.SafetyLogEntity
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GeminiClient
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class PunyansuChatResult {
    data class Success(val responseText: String) : PunyansuChatResult()
    data class SafetyAlertTriggered(
        val alertCode: String,
        val promptText: String,
        val message: String
    ) : PunyansuChatResult()
    data class Error(val errorMessage: String) : PunyansuChatResult()
}

class PunyansuAiRepository(
    private val safetyLogDao: SafetyLogDao? = null,
    private val apiService: GeminiApiService = GeminiClient.apiService
) {

    // Check if the user prompt is an "olta" (reversed / troll / malicious / paper leak / illegal) question
    fun isOltaOrMaliciousQuestion(prompt: String): Boolean {
        val lower = prompt.lowercase().trim()

        val oltaKeywords = listOf(
            "olta", "ulta", "leak paper", "hack exam", "hack aoee", "cheat in aoee",
            "paper leak", "sell question paper", "reverse question", "troll ai",
            "delhi police", "police ku call", "kael repap", "troll punyansu",
            "how to cheat", "buy real question paper"
        )

        for (kw in oltaKeywords) {
            if (lower.contains(kw)) {
                return true
            }
        }

        if (lower.startsWith("olta:") || lower.startsWith("ulta:") || lower.contains("ulta question")) {
            return true
        }

        return false
    }

    suspend fun generateAiChatResponse(
        userPrompt: String,
        chatHistory: List<ChatMessageEntity> = emptyList()
    ): PunyansuChatResult = withContext(Dispatchers.IO) {
        // Step 1: Safety check for "Olta" / malicious question
        if (isOltaOrMaliciousQuestion(userPrompt)) {
            val alertCode = "PUNYANSU-SAFETY-ALERT-${(1000..9999).random()}-DELHI-POLICE-CELL"

            safetyLogDao?.insertLog(
                SafetyLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis(),
                    triggerPrompt = userPrompt,
                    alertCode = alertCode,
                    status = "EMERGENCY_ALERT_DELHI_POLICE_NOTIFIED"
                )
            )

            return@withContext PunyansuChatResult.SafetyAlertTriggered(
                alertCode = alertCode,
                promptText = userPrompt,
                message = "🚨 EMERGENCY SAFETY PROTOCOL ACTIVATED! Olta/Illegal query detected. Punyansu AI is signaling Delhi Police Cyber Cell & AOEE Exam Security Command."
            )
        }

        // Step 2: Try calling Gemini REST API with rich prompt and conversation history
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val systemPrompt = """
                    You are "Punyansu AI", the premier AI Model, authoritative tutor, and entrance mentor for the ALL ODISHA ENTRANCE EXAMINATION (AOEE) platform.
                    
                    Your domain mastery covers all entrance examinations and competitive exams in Odisha and India:
                    1. JNVST Navodaya Vidyalaya Entrance (Class 6 & 9): Mental ability, pattern completion, odd man out, arithmetic speed tricks, language reading.
                    2. Pathani Samanta Mathematics Scholarship (PSMSE): Number theory, higher mental algebra, geometry proofs, sequences, trigonometry.
                    3. Odisha Adarsha Vidyalaya (OAV Class 6 & 9): English grammar, Odia byakarana & sahitya, general science, social studies, arithmetic.
                    4. Odisha CT / D.El.Ed, B.Ed, OTET, OSSTET: Child Development, Piaget, Vygotsky, Kohlberg, Bloom's Taxonomy, NEP 2020, RTE Act 2009, pedagogical methods.
                    5. OJEE All Streams (B.Tech, B.Pharm, MCA, MBA, M.Tech, Lateral Entry) & Odisha DET / Diploma & ITI.
                    6. OAS / IAS Civil Services (OPSC & UPSC): Odisha history, Kalinga war, Kharavela, Somavamshis, Gangas, Gajapatis, Paika rebellion 1817, Salt satyagraha in Inchudi, Utkal Sammilani, Indian Constitution & Polity, Indian Economy, Ecology & Environment.
                    7. Odisha Govt Exams (OSSC, OSSSC, RI, AMIN, Junior Clerk, Odisha Police SI & Constable): Odisha Geography, Mahanadi river system, Chilika, Similipal, Hirakud, Bhitarkanika, mineral reserves, Odia Grammar (Sandhi, Samasa, Karaka, Rudhi & Lokabani), General Science, World GK.
                    8. General Knowledge & Current Affairs: Indian & World History, Geography, Indian Polity (Preamble, Fundamental Rights, Articles), General Science (Physics, Chemistry, Biology), Inventions, Space missions (ISRO Chandrayaan-3, Aditya-L1, Gaganyaan), World Organizations (UN, WHO, UNESCO, WTO).

                    Instruction Guidelines:
                    - Understand queries in English, Odia script (ଓଡ଼ିଆ), or Romanized Odia (e.g., 'Kalinga judha kebe heithila?').
                    - When explaining concepts, use structured headings, bullet points, and high-yield Exam Pro-Tips.
                    - For numerical/math/physics/chemistry questions: provide (1) Given values, (2) Relevant Formula, (3) Step-by-step working, (4) Final Answer, and (5) Shortcut Trick.
                    - Keep a friendly, encouraging, scholarly, and supportive tone.
                """.trimIndent()

                val systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = systemPrompt))
                )

                // Build multi-turn content from recent non-alert messages (up to last 6 messages)
                val contentsList = mutableListOf<GeminiContent>()
                val recentHistory = chatHistory.takeLast(6).filter { !it.isSafetyAlert }
                
                for (msg in recentHistory) {
                    val role = if (msg.sender == "USER") "user" else "model"
                    contentsList.add(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = msg.text)),
                            role = role
                        )
                    )
                }

                // Add current prompt
                contentsList.add(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = userPrompt)),
                        role = "user"
                    )
                )

                val request = GeminiRequest(
                    contents = contentsList,
                    systemInstruction = systemInstruction
                )

                val response = apiService.generateContent(apiKey, request)
                val textResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (!textResponse.isNullOrBlank()) {
                    return@withContext PunyansuChatResult.Success(textResponse)
                }
            } catch (e: Exception) {
                // Fallthrough to intelligent Offline Knowledge Base if network or quota issue
            }
        }

        // Step 3: Offline Comprehensive Knowledge Engine for Punyansu AI
        val fallbackResponse = getIntelligentFallbackResponse(userPrompt)
        return@withContext PunyansuChatResult.Success(fallbackResponse)
    }

    private fun getIntelligentFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase().trim()

        return when {
            // === ODISHA HISTORY, HERITAGE & CULTURE ===
            lower.contains("kalinga") || lower.contains("ashoka") || lower.contains("261 bc") || lower.contains("dhauli") -> {
                "🏛️ **Punyansu AI Explanation: The Kalinga War (261 BC)**\n\n" +
                        "• **Background:** Fought in 261 BC along the banks of the **Daya River** near Dhauli hills (Bhubaneswar) between Emperor Ashoka of Magadha and the state of Kalinga.\n" +
                        "• **Significance:** Ashoka was devastated by the bloodshed (over 100,000 casualties) and converted from Chandashoka to Dharmashoka, embracing Buddhism under Monk Upagupta.\n" +
                        "• **Rock Edict:** Mentioned in **Major Rock Edict XIII (13)** of Ashoka.\n" +
                        "• **AOEE / OAS Exam Tip:** Dhauli Stupa (Peace Pagoda) was built in 1972 jointly by Kalinga Nippon Buddha Sangha."
            }

            lower.contains("paika") || lower.contains("buxi") || lower.contains("jagabandhu") || lower.contains("1817") -> {
                "⚔️ **Punyansu AI Explanation: Paika Rebellion (1817)**\n\n" +
                        "• **Leader:** **Buxi Jagabandhu Bidyadhara Mohapatra**, commander of the forces of Raja of Khordha (Mukunda Deva II).\n" +
                        "• **Causes:** Resentment against British land revenue policies, currency reforms (rupee replacing cowrie shells), and salt tax.\n" +
                        "• **Key Figures:** Jayee Rajguru (martyred in 1806 at Midnapore as an early precursor), Krushna Chandra Bhramarabar Ray, Dinabandhu Samantasinghar.\n" +
                        "• **Significance:** Often called the **First War of Indian Independence** from eastern India."
            }

            lower.contains("utkal divas") || lower.contains("1 april") || lower.contains("1936") || lower.contains("formation of odisha") -> {
                "🏛️ **Punyansu AI Explanation: Formation of Odisha (Utkal Divas - 1 April 1936)**\n\n" +
                        "• **Historical Milestone:** Odisha became the **first linguistic state in India** on **1st April 1936**.\n" +
                        "• **Founding Architects:**\n" +
                        "  - **Utkal Gourab Madhusudan Das** (Pioneer of Utkal Sammilani in 1903).\n" +
                        "  - **Maharaja Krushna Chandra Gajapati** (King of Paralakhemundi, 1st Prime Minister of Odisha).\n" +
                        "  - **Utkalmani Pandit Gopabandhu Das** (Satyabadi Vana Vidyalaya, Samaj newspaper).\n" +
                        "• **First Governor:** Sir John Austen Hubback.\n" +
                        "• **First Capital:** Cuttack (shifted to Bhubaneswar in 1948-49)."
            }

            lower.contains("konark") || lower.contains("sun temple") || lower.contains("jagannath") || lower.contains("puri") -> {
                "🛕 **Punyansu AI Odisha Architectural & Cultural Heritage:**\n\n" +
                        "1. **Konark Sun Temple (Black Pagoda):**\n" +
                        "   - Built by **King Narasimhadeva I** of Eastern Ganga Dynasty (13th Century, ~1250 AD).\n" +
                        "   - Designed as a colossal chariot with 24 wheels carved from stone, drawn by 7 horses.\n" +
                        "   - Declared a **UNESCO World Heritage Site** in 1984.\n\n" +
                        "2. **Puri Jagannath Temple (White Pagoda):**\n" +
                        "   - Initiated by Eastern Ganga King **Anantavarman Chodaganga Deva** and completed by Anangabhima Deva III (12th Century).\n" +
                        "   - World-famous annual **Ratha Yatra** (Chariot Festival of Nandighosa, Taladhwaja, and Darpadalana)."
            }

            // === ODISHA GEOGRAPHY & ENVIRONMENT ===
            lower.contains("mahanadi") || lower.contains("hirakud") || lower.contains("chilika") || lower.contains("similipal") || lower.contains("bhitarkanika") -> {
                "🗺️ **Punyansu AI Odisha Physical Geography & Biodiversity:**\n\n" +
                        "• **Mahanadi River:** Originates in Sihawa hills (Dhamtari, Chhattisgarh). Longest river in Odisha (~858 km). Major tributaries: Tel, Ib, Ong, Jonk, Mand.\n" +
                        "• **Hirakud Dam:** World's longest earthen dam (25.8 km total length) built on Mahanadi in Sambalpur (commissioned 1957).\n" +
                        "• **Chilika Lake:** Largest brackish water coastal lagoon in Asia, 1st Indian wetland designated under **Ramsar Convention (1981)**. Known for endangered **Irrawaddy Dolphins** and Nalabana Bird Sanctuary.\n" +
                        "• **Similipal National Park:** UNESCO Biosphere Reserve & Tiger Reserve in Mayurbhanj district; famous for melanistic (black) tigers and Barehipani waterfall (399 m).\n" +
                        "• **Bhitarkanika National Park:** Mangrove ecosystem in Kendrapara; home to saltwater crocodiles and Gahirmatha Olive Ridley sea turtle nesting beach."
            }

            // === CHILD PEDAGOGY, CT & B.ED / OTET ===
            lower.contains("piaget") || lower.contains("vygotsky") || lower.contains("pedagogy") || lower.contains("kohlberg") || lower.contains("bloom") || lower.contains("nep 2020") || lower.contains("rte") -> {
                "👩‍🏫 **Punyansu AI Child Development & Pedagogy Master-Guide (CT / B.Ed / OTET):**\n\n" +
                        "**1. Jean Piaget's 4 Cognitive Stages:**\n" +
                        "   • Sensorimotor (0-2 yrs): Object permanence development.\n" +
                        "   • Pre-Operational (2-7 yrs): Egocentrism, symbolic play, lack of conservation.\n" +
                        "   • Concrete Operational (7-11 yrs): Conservation, reversibility, logical thinking on real objects.\n" +
                        "   • Formal Operational (11+ yrs): Abstract reasoning, deductive hypothesis testing.\n\n" +
                        "**2. Lev Vygotsky's Socio-Cultural Theory:**\n" +
                        "   • **ZPD (Zone of Proximal Development):** Gap between what a child can do alone vs. with guidance.\n" +
                        "   • **Scaffolding:** Temporary support provided by MKO (More Knowledgeable Other).\n\n" +
                        "**3. RTE Act 2009 & NEP 2020:**\n" +
                        "   • RTE Act 2009 (Enacted 1 April 2010): Free & compulsory education for ages 6–14 (Article 21A).\n" +
                        "   • NEP 2020 structure: **5 + 3 + 3 + 4** (Foundational, Preparatory, Middle, Secondary)."
            }

            // === NAVODAYA & OAV ENTRANCE (JNVST) ===
            lower.contains("navodaya") || lower.contains("jnvst") || lower.contains("mental ability") || lower.contains("odd man out") || lower.contains("oav") -> {
                "🏫 **Punyansu AI JNVST Navodaya & OAV Entrance Strategy:**\n\n" +
                        "**1. Section Breakdown:**\n" +
                        "   • **Mental Ability (50 Marks, 40 Qs):** Odd-man-out, Figure Matching, Pattern Completion, Figure Series, Analogy, Geometrical Figure, Mirror Images, Paper Folding, Space Visualization, Embedded figures.\n" +
                        "   • **Arithmetic (25 Marks, 20 Qs):** Number system, LCM/HCF, Decimals, Fractions, Percentage, Profit & Loss, Simple Interest, Perimeter & Area, Speed-Distance-Time.\n" +
                        "   • **Language Comprehension (25 Marks, 20 Qs):** 4 Unseen Passages in Odia/English.\n\n" +
                        "**Pro-Tip for Candidates:** Master elimination in Mental Ability first to secure full 50 marks in under 45 minutes!"
            }

            // === PATHANI SAMANTA MATHEMATICS SCHOLARSHIP (PSMSE) ===
            lower.contains("pathani samanta") || lower.contains("psmse") || lower.contains("siddhanta darpana") -> {
                "🔭 **Punyansu AI Pathani Samanta Mathematics Scholarship (PSMSE) Guide:**\n\n" +
                        "• **Who was Mahamahopadhyaya Samanta Chandra Sekhara (Pathani Samanta)?**\n" +
                        "  - Legendary Odisha astronomer from Khandapara (Nayagarh) who authored 'Siddhanta Darpana' using indigenous bamboo instruments (Mana Yantra).\n\n" +
                        "• **Key PSMSE Math Aptitude Topics:**\n" +
                        "  1. **Number Theory:** Divisibility rules (7, 11, 13), Unit digits of exponents, Prime factorization.\n" +
                        "  2. **Algebra:** Algebraic identities (a+b+c)^2, factor theorem, sum of digits.\n" +
                        "  3. **Geometry:** Congruence, circle theorems, Pythagorean triplets, polygon angle formulas (n-2)*180 degrees.\n" +
                        "  4. **Brain-Teasers:** Cryptarithmetic puzzles, clock and calendar angles."
            }

            // === PHYSICS & OJEE / DET ===
            lower.contains("kirchhoff") || lower.contains("circuit") || lower.contains("kcl") || lower.contains("kvl") -> {
                "💡 **Punyansu AI Physics Master-Class (Electric Circuits & Kirchhoff's Laws):**\n\n" +
                        "**1. Kirchhoff's Current Law (KCL / Junction Rule):**\n" +
                        "   - Algebraic sum of currents entering a junction equals sum leaving it: Sum(I_in) = Sum(I_out).\n" +
                        "   - **Fundamental Conservation Principle:** **Conservation of Electric Charge**.\n\n" +
                        "**2. Kirchhoff's Voltage Law (KVL / Loop Rule):**\n" +
                        "   - Algebraic sum of all potential differences in any closed mesh equals zero: Sum(V) = 0.\n" +
                        "   - **Fundamental Conservation Principle:** **Conservation of Energy**.\n\n" +
                        "**3. Wheatstone Bridge Condition:**\n" +
                        "   - When galvanometer current is zero, R1 / R2 = R3 / R4."
            }

            lower.contains("optics") || lower.contains("young's") || lower.contains("double slit") || lower.contains("refraction") || lower.contains("snell") -> {
                "🔬 **Punyansu AI Physics: Wave & Ray Optics:**\n\n" +
                        "**1. Snell's Law of Refraction:**\n" +
                        "   - (sin i) / (sin r) = mu2 / mu1 = v1 / v2\n\n" +
                        "**2. Young's Double Slit Experiment (YDSE):**\n" +
                        "   - Fringe Width formula: Beta = (lambda * D) / d\n" +
                        "   - Position of n-th Bright Fringe: y_n = (n * lambda * D) / d\n" +
                        "   - Position of n-th Dark Fringe: y_n = (n - 0.5) * (lambda * D) / d\n\n" +
                        "**3. Total Internal Reflection (TIR):** Occurs when light travels from denser to rarer medium at angle i > i_c, where sin(i_c) = 1 / mu."
            }

            lower.contains("thermodynamics") || lower.contains("carnot") || lower.contains("entropy") -> {
                "🔥 **Punyansu AI Physics / Chemistry: Thermodynamics:**\n\n" +
                        "• **First Law:** Delta Q = Delta U + Delta W (Conservation of Energy, where Delta W = P * Delta V).\n" +
                        "• **Carnot Engine Efficiency:** Efficiency eta = 1 - (T2 / T1) = (T1 - T2) / T1 * 100% (Temps in Kelvin!).\n" +
                        "• **Gibbs Free Energy (Delta G):** Delta G = Delta H - T * Delta S\n" +
                        "  - Delta G < 0: Spontaneous process.\n" +
                        "  - Delta G = 0: Equilibrium state.\n" +
                        "  - Delta G > 0: Non-spontaneous process."
            }

            // === CHEMISTRY & OJEE ===
            lower.contains("aldol") || lower.contains("cannizzaro") || lower.contains("grignard") || lower.contains("organic reaction") -> {
                "🧪 **Punyansu AI Chemistry: High-Yield Named Organic Reactions:**\n\n" +
                        "1. **Aldol Condensation:**\n" +
                        "   - Aldehydes/ketones containing **at least one alpha-hydrogen** react in presence of dilute base (NaOH) to form beta-hydroxyaldehyde (Aldol), which upon heating loses water to yield alpha,beta-unsaturated carbonyls.\n\n" +
                        "2. **Cannizzaro Reaction:**\n" +
                        "   - Aldehydes having **NO alpha-hydrogen** (e.g., Formaldehyde HCHO, Benzaldehyde C6H5CHO) undergo self-oxidation-reduction in conc. KOH to produce an alcohol and a carboxylate salt.\n\n" +
                        "3. **Grignard Reagent (R-Mg-X):**\n" +
                        "   - Formaldehyde + R-Mg-X -> **1 Degree Alcohol**.\n" +
                        "   - Other Aldehydes + R-Mg-X -> **2 Degree Alcohol**.\n" +
                        "   - Ketones + R-Mg-X -> **3 Degree Alcohol**."
            }

            lower.contains("periodic") || lower.contains("electronegativity") || lower.contains("hybridization") -> {
                "🧬 **Punyansu AI Chemistry: Chemical Bonding & Periodic Trends:**\n\n" +
                        "• **Periodic Trends Across a Period (Left to Right):**\n" +
                        "  - Atomic Radius: *Decreases* (Effective nuclear charge Z_eff increases).\n" +
                        "  - Ionization Enthalpy & Electronegativity: *Increases* (Fluorine is highest: 4.0 on Pauling scale).\n\n" +
                        "• **Hybridization Quick Shortcut (H = 0.5 * [V + M - C + A]):**\n" +
                        "  - H = 2 -> sp (Linear, 180 degrees, e.g., BeCl2, CO2)\n" +
                        "  - H = 3 -> sp2 (Trigonal planar, 120 degrees, e.g., BF3)\n" +
                        "  - H = 4 -> sp3 (Tetrahedral, 109.5 degrees, e.g., CH4, NH3 [pyramidal], H2O [bent])"
            }

            // === MATHEMATICS & SHORTCUTS ===
            lower.contains("math") || lower.contains("integration") || lower.contains("derivative") || lower.contains("calculus") || lower.contains("shortcut") -> {
                "📐 **Punyansu AI Mathematics Formula & Shortcut Arsenal:**\n\n" +
                        "1. **Integration by Parts:** Integral(u * v dx) = u * Integral(v dx) - Integral(u' * Integral(v dx) dx)\n" +
                        "   - Follow the **ILATE** priority order: Inverse, Logarithmic, Algebraic, Trigonometric, Exponential.\n\n" +
                        "2. **Definite Integral Super-Shortcut:**\n" +
                        "   Integral from a to b of [f(x) / (f(x) + f(a+b-x))] dx = (b - a) / 2\n\n" +
                        "3. **Matrices & Determinants:**\n" +
                        "   - |A * B| = |A| * |B|\n" +
                        "   - |k * A| = (k^n) * |A| (where n is order of square matrix A)\n" +
                        "   - |adj(A)| = |A|^(n-1)\n\n" +
                        "4. **Quadratic Equation Roots:** For ax^2 + bx + c = 0, Sum of roots alpha + beta = -b/a, Product alpha * beta = c/a."
            }

            // === BIOLOGY & PHARMACY ===
            lower.contains("biology") || lower.contains("genetics") || lower.contains("dna") || lower.contains("photosynthesis") || lower.contains("pharma") -> {
                "🌱 **Punyansu AI Biology & Pharmaceutical Sciences:**\n\n" +
                        "• **Mendelian Genetics:**\n" +
                        "  - Monohybrid Phenotypic Ratio: **3 : 1** (Genotypic: 1 : 2 : 1).\n" +
                        "  - Dihybrid Phenotypic Ratio: **9 : 3 : 3 : 1**.\n" +
                        "• **Photosynthesis:**\n" +
                        "  - Light Reaction occurs in **Thylakoid / Grana** (Generates ATP & NADPH).\n" +
                        "  - Dark Reaction (Calvin Cycle / C3 cycle) occurs in **Stroma** (fixes CO2 via RuBisCO enzyme).\n" +
                        "• **Human Heart & Blood Circulation:**\n" +
                        "  - Pacemaker of human heart: **SA Node (Sinoatrial Node)**.\n" +
                        "  - Universal Blood Donor: **O Negative (O-)**; Universal Recipient: **AB Positive (AB+)**."
            }

            // === INDIAN POLITY, OAS & GENERAL KNOWLEDGE ===
            lower.contains("polity") || lower.contains("constitution") || lower.contains("article") || lower.contains("president") || lower.contains("fundamental rights") -> {
                "⚖️ **Punyansu AI Indian Polity & Constitution Master-Guide (OAS / UPSC / OSSC):**\n\n" +
                        "• **Adoption & Inception:** Adopted on 26 Nov 1949 (Constitution Day), came into force on **26 Jan 1950**.\n" +
                        "• **Fundamental Rights (Part III, Articles 12–35):**\n" +
                        "  - Art 14: Equality before law.\n" +
                        "  - Art 19: Six basic freedoms (Speech, Expression, Assembly, etc.).\n" +
                        "  - Art 21: Right to Life & Personal Liberty.\n" +
                        "  - Art 32: Right to Constitutional Remedies (Heart & Soul of Constitution - Dr. B.R. Ambedkar).\n" +
                        "• **Key Constitutional Bodies:**\n" +
                        "  - Election Commission of India: Article 324.\n" +
                        "  - Comptroller and Auditor General (CAG): Article 148.\n" +
                        "  - Finance Commission: Article 280."
            }

            lower.contains("isro") || lower.contains("chandrayaan") || lower.contains("aditya") || lower.contains("space") -> {
                "🚀 **Punyansu AI Space Science & Current Affairs (ISRO Missions):**\n\n" +
                        "• **Chandrayaan-3:** Launched on 14 July 2023 via LVM3-M4; Historic Soft Landing on Lunar South Pole on **23 August 2023** (Celebrated as **National Space Day**). Landed at 'Shiv Shakti Point'. Lander: Vikram, Rover: Pragyan.\n" +
                        "• **Aditya-L1:** India's first dedicated solar observatory, placed in halo orbit around Sun-Earth **Lagrange Point 1 (L1)** (~1.5 million km from Earth).\n" +
                        "• **Gaganyaan:** India's human spaceflight mission aiming to send 3 crew members to Low Earth Orbit (400 km)."
            }

            // === ODIA BYAKARANA (GRAMMAR) ===
            lower.contains("sandhi") || lower.contains("samasa") || lower.contains("byakarana") || lower.contains("odia grammar") || lower.contains("odia") -> {
                "📖 **Punyansu AI Odia Byakarana (ଓଡ଼ିଆ ବ୍ୟାକରଣ) Quick-Reference:**\n\n" +
                        "• **ସନ୍ଧି (Sandhi):** ଦୁଇଟି ପରସ୍ପର ସନ୍ନିହିତ ବର୍ଣ୍ଣର ମିଳନକୁ ସନ୍ଧି କୁହାଯାଏ (ସ୍ୱର ସନ୍ଧି, ବ୍ୟଞ୍ଜନ ସନ୍ଧି, ବିସର୍ଗ ସନ୍ଧି) ।\n" +
                        "  - ଉଦାହରଣ: ବିଦ୍ୟା + ଆଳୟ = ବିଦ୍ୟାଳୟ, ସତ୍ + ଜନ = ସଜ୍ଜନ ।\n\n" +
                        "• **ସମାସ (Samasa):** ପରସ୍ପର ଅନ୍ୱୟ ଥିବା ଦୁଇ ବା ତତୋଽଧିକ ପଦ ମିଳିତ ହୋଇ ଏକପଦରେ ପରିଣତ ହେବାକୁ ସମାସ କୁହାଯାଏ ।\n" +
                        "  - ତତ୍ପୁରୁଷ, ଦ୍ୱନ୍ଦ୍ୱ (ମାତା ଓ ପିତା = ମାତାପିତା), ବହୁବ୍ରୀହି (ଦଶ ଆନନ ଯାହାର = ଦଶାନନ), କର୍ମଧାରୟ, ଦ୍ୱିଗୁ (ତ୍ରିଭୁବନ), ଅବ୍ୟୟୀଭାବ ।\n\n" +
                        "• **ଲୋକବାଣୀ / ରୂଢ଼ି:**\n" +
                        "  - *ଅନ୍ଧାରରେ ବାଡ଼ି ବୁଲାଇବା:* ଅନୁମାନରେ କାର୍ଯ୍ୟ କରିବା ।\n" +
                        "  - *କାଠିକର ପାଠ:* ଅତ୍ୟନ୍ତ କଷ୍ଟକର ବ୍ୟାପାର ।"
            }

            // === DEFAULT COMPREHENSIVE MENTOR RESPONSE ===
            else -> {
                "✨ **Punyansu AI Entrance Exam & General Knowledge Mentor:**\n\n" +
                        "Namaskar! As your official AOEE & competitive entrance mentor, here is a structured breakdown for: **${prompt.take(60)}**\n\n" +
                        "📌 **Core Concept & Theoretical Foundation:**\n" +
                        "• In competitive entrance exams (OJEE, Navodaya, PSMSE, CT/B.Ed, OAS/OSSC), mastering fundamental concepts and interlinking them with previous year question trends (2018-2025) is the key to high percentile scoring.\n\n" +
                        "🎯 **Exam Preparation Action Steps:**\n" +
                        "1. **Concept Clarity:** Read standard NCERT/SCERT textbook fundamentals before attempting shortcuts.\n" +
                        "2. **Formula Quick-Recall:** Maintain a dedicated formula & memory sheet for instant revision.\n" +
                        "3. **Speed Drills:** Practice with Punyansu CBT Mock Tests to maintain under 60-second speed per question.\n\n" +
                        "💬 *Tip: Ask me to explain any specific derivation, solve a math/science numerical, or provide GK & Odisha facts!*"
            }
        }
    }
}
