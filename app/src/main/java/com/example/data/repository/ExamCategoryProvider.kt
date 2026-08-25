package com.example.data.repository

import com.example.data.model.*

object ExamCategoryProvider {

    fun getCategoryDetailForStream(stream: ExamStream): ExamCategoryDetail {
        return allCategoryDetails.find { it.stream == stream } ?: getCtEntranceDetails()
    }

    fun getAllCategories(): List<ExamCategoryDetail> = allCategoryDetails

    val allCategoryDetails: List<ExamCategoryDetail> = listOf(
        getCtEntranceDetails(),
        getOjeeEngineeringDetails(),
        getNavodayaDetails(),
        getPathaniSamantaDetails(),
        getOavDetails(),
        getDiplomaDetDetails(),
        getOasCivilDetails(),
        getAdultOsouDetails(),
        getTeacherOtetDetails(),
        getMedicalPharmaDetails(),
        getCpetDetails(),
        getSchoolFoundationDetails(),
        getSecondaryChseDetails(),
        getOjeeAllDetails(),
        getOdishaAllGovtDetails(),
        getItiPolytechnicDetails()
    )

    private fun getCtEntranceDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.CT_ENTRANCE,
            tier = "Teacher Education & Pedagogy",
            conductingBody = "Directorate of TE & SCERT, Odisha / BSE Odisha",
            shortCode = "CT / D.El.Ed",
            fullTitle = "Odisha CT / D.El.Ed Teacher Entrance Examination",
            odiaTitle = "ଓଡ଼ିଶା ସି.ଟି / ଡି.ଏଲ୍.ଇଡି ଶିକ୍ଷକ ପ୍ରବେଶିକା ପରୀକ୍ଷା",
            tagLine = "Primary & Upper Primary Elementary Teacher Preparation Program",
            examDurationMinutes = 90,
            totalQuestions = 90,
            totalMarks = 90,
            markingScheme = "+1 Mark for Correct, -0.25 Mark for Wrong",
            examMode = "Computer-Based Test (CBT)",
            examLanguage = "Odia & English Bilingual",
            ageCriteria = "Minimum 17 Years to Maximum 30 Years (Age relaxation for SC/ST/SEBC)",
            qualificationSummary = "10+2 Arts/Science/Commerce with minimum 50% aggregate marks (45% for SC/ST/SEBC/PH).",
            eligibilityRules = listOf(
                "Must have passed Higher Secondary (+2) examination or equivalent with 50% marks (45% for reserved categories).",
                "Candidate must have Odia as MIL up to HSC (Class 10) level or passed Odia test conducted by BSE Odisha.",
                "Permanent resident / domicile of Odisha status required for state quota seats.",
                "Upper age limit relaxed by 5 years for SC, ST, SEBC and Women candidates."
            ),
            subjects = listOf(
                SubjectWeightage("Language (Odia & English)", 20, 20, 22, listOf("Odia Grammar", "Comprehension", "Vocabulary", "Tenses & Prepositions")),
                SubjectWeightage("Child Development & Pedagogy", 30, 30, 33, listOf("Piaget & Vygotsky Theories", "Inclusive Education", "Learning Assessment", "Curriculum Design")),
                SubjectWeightage("General Awareness & Reasoning", 15, 15, 17, listOf("Odisha History & Geography", "Current Affairs", "Logical Deduction", "Analogy")),
                SubjectWeightage("Mathematics & Science", 15, 15, 17, listOf("Number System", "Basic Geometry", "Plant & Animal Biology", "Force & Motion")),
                SubjectWeightage("Social Science", 10, 10, 11, listOf("Indian Freedom Movement", "Indian Constitution", "Odisha Heritage", "Natural Resources"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Child Growth & Cognitive Development", "Pedagogy", listOf("Physical & cognitive milestones", "Piaget's stages of development", "Moral development by Kohlberg", "Language acquisition in children"), 14, "CRITICAL"),
                SyllabusModule("Inclusive Education & Learning Difficulties", "Pedagogy", listOf("Addressing diverse learners", "Catering to gifted and slow learners", "Understanding dyslexia & ADHD", "Classroom adjustment strategies"), 10, "HIGH"),
                SyllabusModule("Odia Bhasa O Byakarana (ଓଡ଼ିଆ ଭାଷା ଓ ବ୍ୟାକରଣ)", "Language", listOf("ସନ୍ଧି ଓ ସମାସ", "କୃଦନ୍ତ ଓ ତଦ୍ଧିତ", "ବାକ୍ୟ ପରିବର୍ତ୍ତନ", "ଅଶୁଦ୍ଧି ସଂଶୋଧନ", "ରୂଢ଼ି ଓ ଲୋକବାଣୀ"), 12, "CRITICAL"),
                SyllabusModule("Elementary Mathematics & Arithmetic", "Mathematics", listOf("HCF & LCM", "Fractions & Decimals", "Ratio & Proportion", "Profit & Loss", "Mensuration of 2D shapes"), 12, "HIGH"),
                SyllabusModule("Basic Physical & Life Sciences", "Science", listOf("Cell structure & functions", "Photosynthesis & respiration", "Newton's laws of motion", "Acids, bases and salts"), 10, "MODERATE")
            ),
            patternSections = listOf(
                PatternSection("Section I: General English, Odia & Reasoning", 30, 1, "-0.25 Mark", 30),
                PatternSection("Section II: Child Pedagogy & Teaching Aptitude", 30, 1, "-0.25 Mark", 30),
                PatternSection("Section III: Subject Knowledge (Math, Science, Social)", 30, 1, "-0.25 Mark", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("General / UR (Science)", "58.50 / 90", "64+ / 90"),
                CutoffStat("General / UR (Arts)", "54.25 / 90", "60+ / 90"),
                CutoffStat("SEBC (OBC)", "51.00 / 90", "56+ / 90"),
                CutoffStat("SC Category", "44.75 / 90", "50+ / 90"),
                CutoffStat("ST Category", "38.50 / 90", "44+ / 90")
            ),
            aiPredictions2026 = listOf(
                "Pedagogy Questions on 'Constructivist Teaching Model' & 'Continuous and Comprehensive Evaluation (CCE)' carry 94% recurring probability.",
                "Odia Grammar will focus on 'Krudanta/Taddhita' derivations and sentence transformation rules from recent BSE board trends.",
                "Math questions are predicted to emphasize standard unitary method, simple percentages, and area perimeter calculations.",
                "Science section will include at least 4 practical application questions on human health, nutrition, and environmental conservation."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("Online CBT Entrance Exam", "May 2026", "Upcoming"),
                TimelineEvent("Answer Key & Objection Window", "June 2026", "Upcoming"),
                TimelineEvent("State Merit Rank List Publication", "July 2026", "Upcoming"),
                TimelineEvent("DIET / BIET College Choice Locking", "July - August 2026", "Upcoming"),
                TimelineEvent("Document Verification & Final Admission", "August 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Piaget's 4 Cognitive Stages" to "Sensory-motor (0-2y), Pre-operational (2-7y), Concrete (7-11y), Formal (11y+)",
                "IQ Formula (Stern & Binet)" to "IQ = (Mental Age / Chronological Age) × 100",
                "Simple Interest Formula" to "SI = (P × R × T) / 100",
                "Speed & Distance" to "Speed = Distance / Time; 1 km/h = 5/18 m/s",
                "Photosynthesis Equation" to "6CO₂ + 6H₂O + Sunlight → C₆H₁₂O₆ + 6O₂"
            )
        )
    }

    private fun getOjeeEngineeringDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.ENGINEERING,
            tier = "Professional & Technical Entrance",
            conductingBody = "Odisha Joint Entrance Examination (OJEE) Committee",
            shortCode = "OJEE B.Tech",
            fullTitle = "Odisha Joint Entrance Examination (OJEE B.Tech / Lateral Entry)",
            odiaTitle = "ଓଡ଼ିଶା ଯୁଗ୍ମ ପ୍ରବେଶିକା ପରୀକ୍ଷା (ବି.ଟେକ୍ ଇଞ୍ଜିନିୟରିଂ)",
            tagLine = "State-level Gateway to Premier Govt & Private Engineering Colleges in Odisha",
            examDurationMinutes = 120,
            totalQuestions = 120,
            totalMarks = 480,
            markingScheme = "+4 Marks for Correct, -1 Mark for Wrong",
            examMode = "Computer-Based Test (CBT)",
            examLanguage = "English",
            ageCriteria = "No upper age limit for B.Tech regular / lateral entry admission",
            qualificationSummary = "Passed 10+2 with Physics & Mathematics as compulsory subjects with 45% aggregate (40% for reserved category).",
            eligibilityRules = listOf(
                "Passed 10+2 examination with Physics and Mathematics as compulsory subjects along with Chemistry/Biotech/Biology/Technical Vocational.",
                "Obtained at least 45% marks (40% in case of candidates belonging to reserved category) in the above subjects taken together.",
                "For Lateral Entry B.Tech: Passed 3-year Diploma in Engineering or B.Sc with Mathematics.",
                "Valid Odisha Resident certificate for state government engineering college quota seats (VSSUT, OUTR, PMEC, GCEK)."
            ),
            subjects = listOf(
                SubjectWeightage("Physics", 40, 160, 33, listOf("Modern Physics", "Electromagnetism", "Optics", "Thermodynamics", "Mechanics")),
                SubjectWeightage("Chemistry", 40, 160, 33, listOf("Coordination Chemistry", "Chemical Kinetics", "Organic Mechanisms", "Periodic Properties")),
                SubjectWeightage("Mathematics", 40, 160, 34, listOf("Calculus (Integral & Differential)", "Vectors & 3D Geometry", "Matrices & Determinants", "Probability"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Calculus & Differential Equations", "Mathematics", listOf("Definite integrals & properties", "First order differential equations", "Continuity & differentiability", "Maxima and minima applications"), 22, "CRITICAL"),
                SyllabusModule("Electrodynamics & Optics", "Physics", listOf("Gauss's law and electric potential", "Electromagnetic induction & AC circuits", "Wave optics and interference", "Ray optics & lens maker formula"), 20, "CRITICAL"),
                SyllabusModule("Coordination Compounds & Organic Reactions", "Chemistry", listOf("Crystal Field Theory & IUPAC nomenclature", "Aldehydes, Ketones and Carboxylic acids", "Electrochemistry & Nernst equation", "Chemical bonding & MOT"), 18, "CRITICAL"),
                SyllabusModule("Vectors, 3D Geometry & Matrices", "Mathematics", listOf("Vector dot and cross product", "Shortest distance between skew lines", "Matrix inverses and Cramer's rule", "Eigenvalues & characteristics"), 16, "HIGH"),
                SyllabusModule("Thermodynamics & Modern Physics", "Physics", listOf("Carnot engine & entropy", "Photoelectric effect & de Broglie wavelength", "Bohr atomic model & hydrogen spectrum", "Semiconductor diodes and logic gates"), 16, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Physics Section", 40, 4, "-1 Mark", 40),
                PatternSection("Chemistry Section", 40, 4, "-1 Mark", 40),
                PatternSection("Mathematics Section", 40, 4, "-1 Mark", 40)
            ),
            cutoffStats = listOf(
                CutoffStat("OUTR Bhubaneswar (CSE)", "Rank 1 - 250 (380+ / 480)", "410+ / 480"),
                CutoffStat("VSSUT Burla (CSE/IT)", "Rank 200 - 650 (340+ / 480)", "365+ / 480"),
                CutoffStat("GCE Keonjhar (Mining/Mech)", "Rank 1500 - 3200 (220+ / 480)", "250+ / 480"),
                CutoffStat("PMEC Berhampur (EE/ECE)", "Rank 1200 - 2800 (240+ / 480)", "270+ / 480"),
                CutoffStat("Top Private Colleges (Silicon, CV Raman)", "Rank 3000 - 8500 (150+ / 480)", "180+ / 480")
            ),
            aiPredictions2026 = listOf(
                "Punyansu AI highlights high recurring frequency in Definite Integral reduction formulas and Matrix Cayley-Hamilton questions.",
                "Physics will feature at least 3 numericals on Photoelectric stopping potential and de Broglie wavelength relations.",
                "Chemistry is weighted towards Coordination d-electron crystal field splitting energy (CFSE) and SN1/SN2 reaction kinetics."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OJEE Online Registration & Exam", "May 2026", "Upcoming"),
                TimelineEvent("Rank Card Declaration", "June 2026", "Upcoming"),
                TimelineEvent("Web Counseling Registration & Mock Allotment", "July 2026", "Upcoming"),
                TimelineEvent("Round 1 Seat Allotment (OUTR, VSSUT, PMEC)", "July 2026", "Upcoming"),
                TimelineEvent("Special OJEE / Spot Round Counseling", "August 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "de Broglie Wavelength" to "λ = h / p = 12.27 / √V Å (for electron)",
                "Lens Maker's Equation" to "1/f = (μ - 1) [1/R₁ - 1/R₂]",
                "Nernst Equation" to "E_cell = E°_cell - (0.0591 / n) log Q",
                "Definite Integral Symmetry" to "∫₀ᵃ f(x) dx = ∫₀ᵃ f(a - x) dx",
                "Matrix Adjoint Determinant" to "|adj A| = |A|^(n-1)"
            )
        )
    }

    private fun getNavodayaDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.NAVODAYA_ENTRANCE,
            tier = "School & Talent Scholarships",
            conductingBody = "Navodaya Vidyalaya Samiti (NVS), Ministry of Education, GoI",
            shortCode = "JNVST",
            fullTitle = "Jawahar Navodaya Vidyalaya Selection Test (JNVST Class 6 & 9)",
            odiaTitle = "ଜବାହର ନବୋଦୟ ବିଦ୍ୟାଳୟ ପ୍ରବେଶିକା ପରୀକ୍ଷା",
            tagLine = "Premier Fully Residential Co-educational Excellence Schools in Every Odisha District",
            examDurationMinutes = 120,
            totalQuestions = 80,
            totalMarks = 100,
            markingScheme = "+1.25 Marks for Correct, No Negative Marking",
            examMode = "OMR Pen-Paper / CBT Format",
            examLanguage = "Odia, English & Hindi",
            ageCriteria = "For Class 6: Age 9-13 years (Studying in Class 5). For Class 9: Age 13-16 years (Studying in Class 8)",
            qualificationSummary = "Candidate must be currently studying in Class V / Class VIII in a recognized school in the same district.",
            eligibilityRules = listOf(
                "Only candidates from the district concerned where the JNV is located are eligible to apply.",
                "Candidate must not have repeated or been detained in Class V / Class VIII in earlier academic years.",
                "75% of the seats in a district are filled by candidates selected from rural areas of the district.",
                "One-third of the total seats are reserved for girl students."
            ),
            subjects = listOf(
                SubjectWeightage("Mental Ability Test (MAT)", 40, 50, 50, listOf("Odd One Out", "Pattern Completion", "Mirror Imaging", "Figure Series", "Space Visualization")),
                SubjectWeightage("Arithmetic Test", 20, 25, 25, listOf("Number System", "LCM & HCF", "Fractions & Decimals", "Percentage", "Simple Interest", "Perimeter & Area")),
                SubjectWeightage("Language Test (Odia / English)", 20, 25, 25, listOf("Passage Comprehension", "Vocabulary", "Grammar Usage", "Synonyms & Antonyms"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Non-Verbal Mental Ability Mastery", "Mental Ability", listOf("Odd-One-Out identification", "Pattern Completion & Embedding", "Mirror & Water Image reflection", "Geometrical Figure Completion (Triangle, Square, Circle)"), 20, "CRITICAL"),
                SyllabusModule("Elementary Arithmetic & Problem Solving", "Arithmetic", listOf("Four fundamental operations", "Factors and multiples including their properties", "Fractional numbers & conversion", "Unitary method, speed & distance", "Profit and loss calculation"), 18, "CRITICAL"),
                SyllabusModule("Odia Language Reading & Comprehension", "Language", listOf("Odisha Folk & Educational Passages", "Drawing inferences from story text", "Identifying noun, pronoun & action words", "Finding contextual synonyms & antonyms"), 14, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Mental Ability Section (40 Questions)", 40, 1, "0 (No negative marking)", 60),
                PatternSection("Arithmetic Section (20 Questions)", 20, 1, "0 (No negative marking)", 30),
                PatternSection("Language Section (20 Questions)", 20, 1, "0 (No negative marking)", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("Rural Boys (General)", "88 - 93 / 100", "95+ / 100"),
                CutoffStat("Rural Girls (General)", "84 - 88 / 100", "90+ / 100"),
                CutoffStat("Urban Candidates", "91 - 96 / 100", "97+ / 100"),
                CutoffStat("SC / ST Candidates", "78 - 84 / 100", "86+ / 100")
            ),
            aiPredictions2026 = listOf(
                "Mental ability will heavily test 'Space Visualization' and 'Paper Folding & Punching' patterns.",
                "Arithmetic questions focus on HCF/LCM word problems (bells ringing together, largest tile sizing) and simple fractional conversions.",
                "Language comprehension passages will draw on Odisha cultural heritage and environmental awareness themes."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("JNVST Written Exam Day", "January - February 2026", "Upcoming"),
                TimelineEvent("NVS District-wise Result Publication", "April 2026", "Upcoming"),
                TimelineEvent("Rural/Urban Domicile & School Record Verification", "May 2026", "Upcoming"),
                TimelineEvent("Medical Fitness & Admission into JNV Campus", "June 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "HCF × LCM Property" to "Product of Two Numbers = HCF × LCM",
                "Average Formula" to "Average = (Sum of all quantities) / (Total number of quantities)",
                "Rectangle Area & Perimeter" to "Area = Length × Breadth; Perimeter = 2 × (Length + Breadth)",
                "Speed Calculation" to "Speed = Distance / Time (Unit: km/h or m/s)"
            )
        )
    }

    private fun getPathaniSamantaDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.PATHANI_SAMANTA,
            tier = "School & Talent Scholarships",
            conductingBody = "Board of Secondary Education (BSE), Odisha",
            shortCode = "PSMSE",
            fullTitle = "Pathani Samanta Mathematics Scholarship Examination (PSMSE)",
            odiaTitle = "ପଠାଣି ସାମନ୍ତ ଗଣିତ ପ୍ରତିଭା ବୃତ୍ତି ପରୀକ୍ଷା",
            tagLine = "Prestigious State Mathematics Talent Search in honor of Mahamahopadhyaya Samanta Chandrasekhar",
            examDurationMinutes = 120,
            totalQuestions = 60,
            totalMarks = 150,
            markingScheme = "+2.5 Marks for Correct, No Negative Marking",
            examMode = "OMR Pen-Paper / CBT Format",
            examLanguage = "Odia & English",
            ageCriteria = "Stage I: Class 6 students; Stage II: Class 9 students (Studying in Odisha recognized schools)",
            qualificationSummary = "Candidate must have secured minimum 50% marks in Mathematics in the previous annual examination.",
            eligibilityRules = listOf(
                "Stage-I: Open to students reading in Class-VI having scored at least 50% in Class-V Mathematics.",
                "Stage-II: Open to students reading in Class-IX having scored at least 50% in Class-VIII Mathematics.",
                "Examination conducted by BSE Odisha across all block and district nodal centers.",
                "Top rankers receive monthly state scholarship and certificate of excellence."
            ),
            subjects = listOf(
                SubjectWeightage("Arithmetic & Number Theory", 20, 50, 33, listOf("Divisibility Rules", "Primes & Composites", "Modular Arithmetic", "Exponents & Powers")),
                SubjectWeightage("Geometry & Mensuration", 20, 50, 33, listOf("Triangle Properties", "Circle Theorems", "Pythagoras Applications", "Symmetry & Angles")),
                SubjectWeightage("Algebra, Logic & Mathematical Reasoning", 20, 50, 34, listOf("Linear Equations", "Polynomial Identities", "Pigeonhole Principle", "Combinatorics"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Number Theory & Olympiad Arithmetic", "Mathematics", listOf("Prime factorization & GCD algorithms", "Remainder theorems & repeating decimals", "Surds, Indices and square roots", "Number base systems"), 18, "CRITICAL"),
                SyllabusModule("Euclidean Geometry & Theorem Proofs", "Geometry", listOf("Congruence and similarity of triangles", "Chords, tangents and cyclic quadrilaterals", "Angles in polygons and parallel transversals", "Area formulas of composite polygons"), 20, "CRITICAL"),
                SyllabusModule("Algebraic Identities & Logic Puzzles", "Algebra", listOf("Expansion of (a+b+c)², a³±b³", "Solving simultaneous equations", "Pattern sequences & series sums", "Logical deductions & math riddles"), 16, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Part A: Objective Single Choice Questions (30 Qs)", 30, 2, "0", 50),
                PatternSection("Part B: Advanced Multi-Step Problem Solving (30 Qs)", 30, 3, "0", 70)
            ),
            cutoffStats = listOf(
                CutoffStat("State Top 100 Scholarship Ranks", "125+ / 150", "135+ / 150"),
                CutoffStat("District Merit Quota", "105+ / 150", "118+ / 150"),
                CutoffStat("Block Level Recognition", "90+ / 150", "100+ / 150")
            ),
            aiPredictions2026 = listOf(
                "High probability of questions on algebraic substitution tricks: x + 1/x = k => x² + 1/x² = k² - 2 and x³ + 1/x³ = k³ - 3k.",
                "Geometry will test cyclic quadrilateral Ptolemy and angle-in-the-same-segment properties.",
                "Number theory will feature divisibility by 7, 11, 13 and unit digit cycles of powers."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("BSE Odisha PSMSE Exam Day", "February 2026", "Upcoming"),
                TimelineEvent("Evaluation & Scorecard Upload", "April 2026", "Upcoming"),
                TimelineEvent("Scholarship Award Ceremony", "August 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Algebra Identity" to "(a + b)² - (a - b)² = 4ab",
                "Pythagorean Triplets" to "(2m, m² - 1, m² + 1) where m > 1",
                "Sum of First n Natural Numbers" to "S_n = [n(n + 1)] / 2",
                "Sum of Interior Angles of Polygon" to "Sum = (n - 2) × 180°"
            )
        )
    }

    private fun getOavDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.OAV_ENTRANCE,
            tier = "School & Talent Scholarships",
            conductingBody = "Odisha Adarsha Vidyalaya Sangathan (OAVS) / BSE Odisha",
            shortCode = "OAVS Entrance",
            fullTitle = "Odisha Adarsha Vidyalaya Entrance Examination (OAV Class 6 & 9)",
            odiaTitle = "ଓଡ଼ିଶା ଆଦର୍ଶ ବିଦ୍ୟାଳୟ ପ୍ରବେଶିକା ପରୀକ୍ଷା",
            tagLine = "English Medium Model CBSE Schools for Rural & Semi-Urban Odisha Talents",
            examDurationMinutes = 120,
            totalQuestions = 60,
            totalMarks = 60,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "OMR Pen-Paper Format",
            examLanguage = "Odia & English Bilingual",
            ageCriteria = "Class 6: 10 to 12 Years as on 31st March; Class 9: 13 to 15 Years",
            qualificationSummary = "Studying in Class 5 (for Class 6 entry) or Class 8 (for Class 9 entry) in any recognized school in Odisha.",
            eligibilityRules = listOf(
                "Student must be a permanent resident of the concerned block of Odisha where OAV is situated.",
                "Must be continuing study in Class 5 or Class 8 in Govt / Aided / Recognized private school.",
                "50% of the total seats are reserved for female students.",
                "Reservation for SC, ST, SEBC and PwD as per Odisha state reservation guidelines."
            ),
            subjects = listOf(
                SubjectWeightage("English", 15, 15, 25, listOf("Grammar", "Comprehension", "Sentence Correction", "Spelling")),
                SubjectWeightage("Odia", 15, 15, 25, listOf("Bhasa Byakarana", "Padya/Gadhya", "Biparita Shabda", "Rudhi")),
                SubjectWeightage("Mathematics", 15, 15, 25, listOf("Number System", "Basic Geometry", "Decimals & Fractions", "Word Problems")),
                SubjectWeightage("Science & Social Studies", 15, 15, 25, listOf("Ecosystems", "Human Body", "Odisha Geography", "Civics"))
            ),
            syllabusModules = listOf(
                SyllabusModule("English & Odia Language Foundation", "Language", listOf("Articles, prepositions & pronouns", "Odia Sandhi & Samasa basics", "Reading unseen passages", "Punctuation & vocabulary"), 16, "CRITICAL"),
                SyllabusModule("Class 5 Mathematics & Geometry", "Mathematics", listOf("Operations on large numbers", "Fractions, decimals and percentages", "Perimeter of rectangle and square", "Time, money and measurements"), 16, "CRITICAL"),
                SyllabusModule("General Science & Environmental Studies", "Science", listOf("Plant parts and functions", "Food chains & nutrition", "Water cycle & conservation", "Earth and space basics"), 14, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("English Section (15 Qs)", 15, 1, "0", 30),
                PatternSection("Odia Section (15 Qs)", 15, 1, "0", 30),
                PatternSection("Mathematics Section (15 Qs)", 15, 1, "0", 30),
                PatternSection("Science & Social Section (15 Qs)", 15, 1, "0", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("Block Merit (General Boys)", "48 - 54 / 60", "55+ / 60"),
                CutoffStat("Block Merit (General Girls)", "44 - 50 / 60", "52+ / 60"),
                CutoffStat("SC / ST Candidates", "38 - 44 / 60", "46+ / 60")
            ),
            aiPredictions2026 = listOf(
                "OAV 2026 exam pattern focuses 50% on language fluency (Odia + English) and 50% on STEM fundamentals.",
                "Math questions will highlight unit conversion (grams to kg, meters to km) and perimeter word problems.",
                "Science section features human organ systems and Odisha wildlife sanctuaries."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OAV Entrance Examination", "March 2026", "Upcoming"),
                TimelineEvent("Merit List Publication by OAVS", "April 2026", "Upcoming"),
                TimelineEvent("Block-Level Admission & Uniform/Book Distribution", "May 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Perimeter of Square" to "4 × Side Length",
                "Area of Rectangle" to "Length × Breadth",
                "Speed Formula" to "Distance ÷ Time",
                "Volume of Cube" to "Side × Side × Side = a³"
            )
        )
    }

    private fun getDiplomaDetDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.DIPLOMA_DET,
            tier = "Professional & Technical Entrance",
            conductingBody = "Directorate of Technical Education & Training (DTET) / SCTE&VT, Odisha",
            shortCode = "Odisha DET",
            fullTitle = "Odisha Diploma Entrance Test (DET / Polytechnic Admission)",
            odiaTitle = "ଓଡ଼ିଶା ଡିପ୍ଲୋମା ପ୍ରବେଶିକା ପରୀକ୍ଷା (ପଲିଟେକନିକ୍)",
            tagLine = "Admission into Govt & Private Polytechnic Engineering Institutes across Odisha",
            examDurationMinutes = 90,
            totalQuestions = 100,
            totalMarks = 100,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "CBT / Merit Counseling",
            examLanguage = "English & Odia",
            ageCriteria = "Minimum 14 years old. No upper age limit for diploma engineering",
            qualificationSummary = "Passed High School Certificate (HSC / Class 10) examination with minimum 35% marks in aggregate.",
            eligibilityRules = listOf(
                "Passed Class 10 examination conducted by BSE Odisha or equivalent board with minimum 35% marks.",
                "Must have passed Mathematics and Science as compulsory subjects in Class 10.",
                "For Lateral Entry (2nd Year Diploma): Passed +2 Science with PCM or passed 2-year ITI trade course.",
                "Must be a citizen of India and permanent resident of Odisha."
            ),
            subjects = listOf(
                SubjectWeightage("Physics (Class 10)", 30, 30, 30, listOf("Electricity & Ohm's Law", "Light Reflection & Refraction", "Magnetic Effects of Current", "Sources of Energy")),
                SubjectWeightage("Chemistry (Class 10)", 30, 30, 30, listOf("Chemical Reactions & Equations", "Acids, Bases & Salts", "Metals & Non-metals", "Carbon & its Compounds")),
                SubjectWeightage("Mathematics (Class 10)", 40, 40, 40, listOf("Quadratic Equations", "Arithmetic Progression", "Trigonometry Basics", "Coordinate Geometry", "Mensuration"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Class 10 Physics & Electrical Principles", "Physics", listOf("Ohm's law, series and parallel resistors", "Electric power and Joule's heating effect", "Mirror and lens equations with magnification", "Magnetic field lines & Fleming's left hand rule"), 18, "CRITICAL"),
                SyllabusModule("Class 10 Chemistry & Materials", "Chemistry", listOf("Balancing chemical equations & redox reactions", "pH scale & salts (Plaster of Paris, Bleaching powder)", "Reactivity series of metals & metallurgy", "Covalent bonding in hydrocarbons"), 16, "CRITICAL"),
                SyllabusModule("Class 10 Mathematics Mastery", "Mathematics", listOf("Roots of quadratic equations by quadratic formula", "n-th term and sum of n terms in AP", "Trigonometric ratios of standard angles (0°, 30°, 45°, 60°, 90°)", "Distance formula and section formula"), 20, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Physics Section (30 Questions)", 30, 1, "0", 25),
                PatternSection("Chemistry Section (30 Questions)", 30, 1, "0", 25),
                PatternSection("Mathematics Section (40 Questions)", 40, 1, "0", 40)
            ),
            cutoffStats = listOf(
                CutoffStat("Govt Polytechnic Bhubaneswar (Mechanical/Civil)", "82+ / 100", "88+ / 100"),
                CutoffStat("BOSE Cuttack (Electrical/CSE)", "78+ / 100", "84+ / 100"),
                CutoffStat("SKDAV Rourkela (Metallurgy/Chemical)", "70+ / 100", "76+ / 100"),
                CutoffStat("Govt Polytechnic Berhampur", "65+ / 100", "72+ / 100")
            ),
            aiPredictions2026 = listOf(
                "Focus heavily on Joule's Heating formula H = I²Rt and equivalent resistance calculation for bridge circuits.",
                "Math questions are predicted to emphasize Quadratic Discriminant D = b² - 4ac conditions.",
                "Chemistry questions will target chemical names and formulas of Plaster of Paris (CaSO4·1/2H2O) and Bleaching Powder."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("DET Online Application Registration", "May 2026", "Upcoming"),
                TimelineEvent("State Merit List Declaration", "June 2026", "Upcoming"),
                TimelineEvent("Polytechnic Choice Locking & Seat Allotment", "July 2026", "Upcoming"),
                TimelineEvent("Reporting at Allotted Polytechnic Institute", "August 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Ohm's Law & Power" to "V = IR; P = VI = I²R = V²/R",
                "Equivalent Resistors" to "Series: R_s = R₁ + R₂; Parallel: 1/R_p = 1/R₁ + 1/R₂",
                "Quadratic Formula" to "x = [-b ± √(b² - 4ac)] / (2a)",
                "Trig Standard Identity" to "sin²θ + cos²θ = 1; 1 + tan²θ = sec²θ"
            )
        )
    }

    private fun getOasCivilDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.OAS_IAS_CIVIL,
            tier = "Civil Services & State Govt",
            conductingBody = "Odisha Public Service Commission (OPSC) / UPSC",
            shortCode = "OPSC OAS",
            fullTitle = "Odisha Civil Services Examination (OAS / OFS / OPS / OCS)",
            odiaTitle = "ଓଡ଼ିଶା ପ୍ରଶାସନିକ ସେବା ପରୀକ୍ଷା (OPSC OAS / IAS)",
            tagLine = "Premier Administrative State Civil Services for Odisha Governance",
            examDurationMinutes = 120,
            totalQuestions = 100,
            totalMarks = 200,
            markingScheme = "+2 Marks for Correct, -0.66 Mark for Wrong",
            examMode = "OMR Prelims followed by Descriptive Mains & Viva",
            examLanguage = "English & Odia",
            ageCriteria = "21 to 38 Years as of 1st January (Relaxation for SC/ST/SEBC/Women up to 43 years)",
            qualificationSummary = "Bachelor's Degree in any discipline from a recognized University.",
            eligibilityRules = listOf(
                "Must hold a Bachelor's degree from any university incorporated by an Act of Central or State Legislature.",
                "Candidate must be able to read, write and speak Odia fluently and passed Middle School exam with Odia as language subject.",
                "Citizen of India with sound health and good moral character.",
                "Number of attempts: 6 for General, 9 for SEBC, Unlimited for SC/ST within age limits."
            ),
            subjects = listOf(
                SubjectWeightage("Odisha History, Heritage & Culture", 25, 50, 25, listOf("Kalinga War & Ashoka", "Ganga & Gajapati Dynasties", "Paika Rebellion 1817", "Jagannath Temple Culture", "Odia Language Movement")),
                SubjectWeightage("Indian Polity & Governance", 20, 40, 20, listOf("Preamble & Fundamental Rights", "President & Governor Powers", "Panchayati Raj in Odisha", "Judicial Review")),
                SubjectWeightage("Geography of India & Odisha", 20, 40, 20, listOf("Mahanadi River System", "Mineral Resources & Mining", "Chilika Lake & Mangroves", "Climate & Cyclones")),
                SubjectWeightage("Economic Development & Ecology", 20, 40, 20, listOf("Odisha Budget & 5T Initiatives", "Poverty & Tribal Welfare", "Biodiversity & Climate Action", "Sustainable Development Goals")),
                SubjectWeightage("General Science & Current Affairs", 15, 30, 15, listOf("National & Odisha Schemes", "Space & Defense Tech", "Environmental Conventions", "Awards & Personalities"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Odisha History, Freedom Struggle & Paika Bidroha", "Odisha Studies", listOf("Buxi Jagabandhu & 1817 Paika Rebellion", "Creation of Odisha Province 1936 (Utkal Gourav Madhusudan Das)", "Gopabandhu Das & Satyabadi Vana Vidyalaya", "Temple architecture of Konark & Lingaraj"), 24, "CRITICAL"),
                SyllabusModule("Indian Constitution, Polity & Administration", "Polity", listOf("Articles 14-32 Fundamental Rights", "Odisha Legislative Assembly & State Cabinet", "73rd & 74th Constitutional Amendments (Gram Panchayats)", "Statutory bodies (OPSC, Lokayukta, State Election Commission)"), 22, "CRITICAL"),
                SyllabusModule("Geography, Climate & Disasters of Odisha", "Geography", listOf("Geomorphic divisions of Odisha (Coastal plains, Eastern Ghats)", "Disaster management models (ODRAF, Cyclone preparedness)", "Biosphere reserves: Similipal & Bhitarkanika", "Mineral belts of Keonjhar, Mayurbhanj & Koraput"), 20, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Paper I: General Studies (100 Qs)", 100, 2, "-0.66 Mark", 120),
                PatternSection("Paper II: CSAT Aptitude & English (80 Qs - Qualifying 33%)", 80, 2.5.toInt(), "-0.83 Mark", 120)
            ),
            cutoffStats = listOf(
                CutoffStat("General / UR Prelims", "104 - 112 / 200", "120+ / 200"),
                CutoffStat("SEBC Category", "96 - 104 / 200", "112+ / 200"),
                CutoffStat("SC Category", "82 - 90 / 200", "98+ / 200"),
                CutoffStat("ST Category", "70 - 78 / 200", "85+ / 200")
            ),
            aiPredictions2026 = listOf(
                "OPSC OAS 2026 will emphasize Odisha-specific questions (at least 35-40% of Paper I).",
                "Deep questions anticipated on Similipal Tiger Reserve ecology, Paika Rebellion historical commissions, and 5T governance reforms.",
                "Polity questions will focus on Governor's discretionary powers and Center-State federal relations."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OPSC OAS Prelims Examination", "August 2026", "Upcoming"),
                TimelineEvent("Prelims Results & Mains Shortlisting", "October 2026", "Upcoming"),
                TimelineEvent("OAS Written Mains Descriptive Examination", "January 2027", "Upcoming"),
                TimelineEvent("Personality Test (Interview) at OPSC Cuttack", "April 2027", "Upcoming")
            ),
            keyFormulas = listOf(
                "Kalinga War Date" to "261 BC (Emperor Ashoka on banks of Daya River)",
                "Separate Odisha Province" to "1st April 1936 (First linguistic state in India)",
                "Fundamental Rights Articles" to "Articles 12 to 35 in Part III of Indian Constitution",
                "Highest Mountain Peak in Odisha" to "Deomali (1,672 m) in Koraput District"
            )
        )
    }

    private fun getAdultOsouDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.ADULT_CONTINUING_ED,
            tier = "Degree & Lifelong Education",
            conductingBody = "Odisha State Open University (OSOU), Sambalpur",
            shortCode = "OSOU Adult Ed",
            fullTitle = "OSOU Adult & Continuing Education Entrance (Up to 55+ Yrs)",
            odiaTitle = "ଓଡ଼ିଶା ରାଜ୍ୟ ମୁକ୍ତ ବିଶ୍ୱବିଦ୍ୟାଳୟ (ବୟସ୍କ ଓ ନିରନ୍ତର ଶିକ୍ଷା)",
            tagLine = "Lifelong Higher Learning, Skill Certification & Degree Completion for All Adults",
            examDurationMinutes = 90,
            totalQuestions = 60,
            totalMarks = 60,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "Online CBT / Distance Enrollment",
            examLanguage = "Odia & English",
            ageCriteria = "Open to Adult Learners up to 55+ Years Old (No restrictive upper age cap)",
            qualificationSummary = "10th / 10+2 / Prior Learning Equivalency for Certificate, Diploma & Bachelor's Degrees.",
            eligibilityRules = listOf(
                "No upper age bar — designed specifically to empower working adults, homemakers, and senior learners up to 55+ years old.",
                "Flexible entry-exit model under National Education Policy (NEP) credit transfer.",
                "Self-learning study material (SLM) provided in both Odia and English.",
                "Weekend counseling sessions available across 50+ study centers across all districts of Odisha."
            ),
            subjects = listOf(
                SubjectWeightage("Digital Literacy & Modern Tech", 15, 15, 25, listOf("Smartphones & Internet Basics", "UPI & Cyber Safety", "MS Office / Google Docs", "Online Public Services")),
                SubjectWeightage("Odia & English Communication", 15, 15, 25, listOf("Everyday Vocabulary", "Drafting Applications & Letters", "Reading Comprehension", "Spoken English Tips")),
                SubjectWeightage("Odisha Heritage, Society & GK", 15, 15, 25, listOf("Odisha Culture & Literature", "Social Welfare Schemes", "Health & Nutrition", "Environmental Hygiene")),
                SubjectWeightage("General Aptitude & Problem Solving", 15, 15, 25, listOf("Basic Calculations", "Logical Reasoning", "Data Reading", "Time Management"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Practical Digital Literacy & Cyber Hygiene", "Digital Skills", listOf("Using Aadhaar, DigiLocker & Odisha citizen portals", "Secure online banking & avoiding UPI fraud", "Email management and searching information online", "Smart digital documentation"), 14, "CRITICAL"),
                SyllabusModule("Functional Odia & English Communication", "Language", listOf("Writing formal letters to local authorities", "Understanding government notices & forms", "Basic conversational English for daily situations", "Correct spelling & grammar in Odia"), 12, "CRITICAL"),
                SyllabusModule("Odisha Governance & Everyday Civic Awareness", "Social Awareness", listOf("Structure of Gram Panchayat & Municipalities", "Key government welfare schemes (Kalia, Biju Swasthya Kalyan)", "Consumer rights & RTI filing basics", "First aid & senior wellness care"), 12, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Digital Skills Section (15 Qs)", 15, 1, "0", 25),
                PatternSection("Language & Communication (15 Qs)", 15, 1, "0", 25),
                PatternSection("General Knowledge & Civics (15 Qs)", 15, 1, "0", 20),
                PatternSection("Basic Aptitude & Logic (15 Qs)", 15, 1, "0", 20)
            ),
            cutoffStats = listOf(
                CutoffStat("Admission Qualifying Score", "24 / 60 (40%)", "35+ / 60"),
                CutoffStat("Merit Scholarship Category", "45 / 60", "50+ / 60")
            ),
            aiPredictions2026 = listOf(
                "Punyansu AI provides specially simplified bilingual explanations for adult candidates resuming study after long gaps.",
                "Assessment places high value on practical real-life scenarios (UPI safety, digital applications, government scheme awareness).",
                "Flexible practice tests allow candidates to learn at their own pace without exam stress."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OSOU Distance Admission Window", "January & July Cycles", "Upcoming"),
                TimelineEvent("Study Center Allocation & SLM Dispatch", "Within 15 Days of Admission", "Upcoming"),
                TimelineEvent("Weekend Contact Sessions & Online Doubts", "Ongoing Monthly", "Upcoming"),
                TimelineEvent("Term-End Examination (TEE)", "June / December", "Upcoming")
            ),
            keyFormulas = listOf(
                "OSOU Headquarters" to "Sambalpur, Odisha (Established 2015 by Odisha Act)",
                "Cyber Safety Golden Rule" to "Never share OTP, UPI PIN, or bank passwords with anyone",
                "RTI Time Limit" to "30 days for general information; 48 hours for life and liberty",
                "CPU Function" to "Central Processing Unit executes all computer program instructions"
            )
        )
    }

    private fun getTeacherOtetDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.TEACHER_OTET,
            tier = "Teacher Education & Pedagogy",
            conductingBody = "Board of Secondary Education (BSE), Odisha",
            shortCode = "OTET / B.Ed",
            fullTitle = "Odisha Teacher Eligibility Test (OTET Paper I & II / B.Ed)",
            odiaTitle = "ଓଡ଼ିଶା ଶିକ୍ଷକ ଯୋଗ୍ୟତା ପରୀକ୍ଷା (OTET / B.Ed)",
            tagLine = "Mandatory State Eligibility Gateway for Upper Primary & High School Teachers",
            examDurationMinutes = 150,
            totalQuestions = 150,
            totalMarks = 150,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "OMR / CBT Format",
            examLanguage = "Odia & English",
            ageCriteria = "No upper age limit for OTET examination",
            qualificationSummary = "Graduation with B.Ed or Senior Secondary (+2) with D.El.Ed / B.El.Ed.",
            eligibilityRules = listOf(
                "Paper I: For candidates aspiring to be teachers in Classes I to V (Primary).",
                "Paper II: For candidates aspiring to be teachers in Classes VI to VIII (Upper Primary).",
                "Minimum qualifying marks: 60% (90/150) for General, 50% (75/150) for SC/ST/OBC/SEBC/PH.",
                "OTET Certificate validity is for lifetime as per NCTE & Odisha Govt norms."
            ),
            subjects = listOf(
                SubjectWeightage("Child Development & Pedagogy", 30, 30, 20, listOf("Theories of Learning", "Evaluation & Assessment", "Child Psychology", "Classroom Diversity")),
                SubjectWeightage("Language I (Odia)", 30, 30, 20, listOf("Odia Sahitya", "Byakarana", "Pedagogy of Odia Language", "Reading Comprehension")),
                SubjectWeightage("Language II (English)", 30, 30, 20, listOf("English Grammar", "Phonetics & Tenses", "Pedagogy of Language Learning", "Unseen Passage")),
                SubjectWeightage("Mathematics / Science / Social Studies", 60, 60, 40, listOf("Curriculum Content", "Methods of Teaching", "Diagnostic & Remedial Teaching"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Child Psychology & Educational Theories", "Pedagogy", listOf("Bruner, Skinner & Thorndike conditioning", "Multiple intelligence by Howard Gardner", "Formative vs Summative Assessment", "National Education Policy (NEP 2020)"), 18, "CRITICAL"),
                SyllabusModule("Odia Language Pedagogy & Grammar", "Odia", listOf("ଭାଷା ଶିକ୍ଷାଦାନ ପଦ୍ଧତି (Methods of teaching Odia)", "ମୂଲ୍ୟାଙ୍କନ ଓ ପରୀକ୍ଷଣ", "ପଦ୍ୟ ଓ ଗଦ୍ୟ ଅବବୋଧ", "ସମାସ, ସନ୍ଧି, ବିଭକ୍ତି ଓ କାରକ"), 16, "CRITICAL"),
                SyllabusModule("Content & Pedagogy of Science / Math / Social", "Domain", listOf("Inquiry-based science learning", "Inductive-deductive methods in mathematics", "Historical thinking & geography field study", "Laboratory work & safety"), 18, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Child Development & Pedagogy (30 Qs)", 30, 1, "0", 30),
                PatternSection("Language I Odia (30 Qs)", 30, 1, "0", 30),
                PatternSection("Language II English (30 Qs)", 30, 1, "0", 30),
                PatternSection("Subject Specialization (60 Qs)", 60, 1, "0", 60)
            ),
            cutoffStats = listOf(
                CutoffStat("General Category Qualifying Cutoff (60%)", "90 / 150", "105+ / 150"),
                CutoffStat("SC / ST / SEBC Qualifying Cutoff (50%)", "75 / 150", "90+ / 150")
            ),
            aiPredictions2026 = listOf(
                "NEP 2020 foundational literacy & numeracy (FLN) and experiential learning will feature in 8-10 pedagogy questions.",
                "Odia pedagogy will test remedial teaching strategies for bilingual tribal students.",
                "Mathematics pedagogical content knowledge will focus on error analysis and misconception remediation."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("BSE Odisha OTET Exam Window", "July 2026", "Upcoming"),
                TimelineEvent("Answer Key & Challenge Filing", "August 2026", "Upcoming"),
                TimelineEvent("Lifetime Certificate Issuance", "September 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Gardner's 8 Multiple Intelligences" to "Linguistic, Logical-Math, Spatial, Bodily, Musical, Interpersonal, Intrapersonal, Naturalist",
                "Bloom's Taxonomy Levels" to "Remembering → Understanding → Applying → Analyzing → Evaluating → Creating",
                "Formative vs Summative" to "Formative is Assessment FOR learning; Summative is Assessment OF learning",
                "RTE Act 2009 Mandate" to "Free and compulsory education for children aged 6 to 14 years"
            )
        )
    }

    private fun getMedicalPharmaDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.MEDICAL_PHARMA,
            tier = "Professional & Technical Entrance",
            conductingBody = "Odisha Joint Entrance Examination (OJEE) Board",
            shortCode = "OJEE B.Pharm",
            fullTitle = "OJEE B.Pharm & Paramedical Entrance Examination",
            odiaTitle = "ଓଡ଼ିଶା ବି.ଫାର୍ମାସି ଓ ପାରାମେଡିକାଲ୍ ପ୍ରବେଶିକା ପରୀକ୍ଷା",
            tagLine = "Admission into Pharmacy, Nursing & Allied Paramedical Degree Colleges in Odisha",
            examDurationMinutes = 120,
            totalQuestions = 120,
            totalMarks = 480,
            markingScheme = "+4 Marks for Correct, -1 Mark for Wrong",
            examMode = "Computer-Based Test (CBT)",
            examLanguage = "English",
            ageCriteria = "Minimum 17 years completed as on 31st December",
            qualificationSummary = "Passed 10+2 with Physics and Chemistry as compulsory subjects along with Biology or Mathematics with 45% marks.",
            eligibilityRules = listOf(
                "Passed 10+2 with Physics, Chemistry and Biology/Mathematics with at least 45% aggregate marks (40% for SC/ST).",
                "Both Biology and Mathematics stream students are eligible for B.Pharm entry.",
                "Odisha state residency required for government pharmacy quota seats.",
                "Rank holder counseling conducted via OJEE centralized online counseling."
            ),
            subjects = listOf(
                SubjectWeightage("Physics", 40, 160, 33, listOf("Mechanics", "Thermodynamics", "Optics", "Current Electricity", "Modern Physics")),
                SubjectWeightage("Chemistry", 40, 160, 33, listOf("Organic Chemistry", "Biomolecules", "Medicinal Chemistry Basics", "Physical Chemistry")),
                SubjectWeightage("Biology / Mathematics", 40, 160, 34, listOf("Human Physiology", "Genetics & Evolution", "Ecology", "Plant Physiology"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Human Physiology & Cell Biology", "Biology", listOf("Digestive, circulatory and nervous systems", "Cellular organelles & ATP synthesis", "Hormonal regulation and endocrinology", "Immune response and antibodies"), 20, "CRITICAL"),
                SyllabusModule("Organic Reactions & Biomolecules", "Chemistry", listOf("Structure and reactions of Carbohydrates & Proteins", "Named organic reactions (Aldol, Cannizzaro, Sandmeyer)", "Polymers and pharmaceuticals", "Stereochemistry of drugs"), 20, "CRITICAL"),
                SyllabusModule("Physics for Medical & Pharma", "Physics", listOf("Fluid mechanics, surface tension and viscosity", "Ray optics, microscopy & resolution", "Radioactivity and nuclear physics", "Wave motion and sound acoustics"), 18, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Physics Section (40 Qs)", 40, 4, "-1 Mark", 40),
                PatternSection("Chemistry Section (40 Qs)", 40, 4, "-1 Mark", 40),
                PatternSection("Biology/Math Section (40 Qs)", 40, 4, "-1 Mark", 40)
            ),
            cutoffStats = listOf(
                CutoffStat("Govt College of Pharmacy (Bhubaneswar)", "360+ / 480", "390+ / 480"),
                CutoffStat("Berhampur University Pharmacy Dept", "310+ / 480", "340+ / 480"),
                CutoffStat("Private Pharmacy Colleges in Odisha", "160+ / 480", "200+ / 480")
            ),
            aiPredictions2026 = listOf(
                "Biology questions will heavily target DNA replication enzymes, Mendelian genetic crosses, and immunity concepts.",
                "Chemistry will focus on reaction intermediates (carbocations) and drug structure-activity concepts.",
                "Physics emphasizes fluid viscosity, Poiseuille's law, and radioactive half-life calculations."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OJEE Pharmacy CBT Exam", "May 2026", "Upcoming"),
                TimelineEvent("Rank Card Release", "June 2026", "Upcoming"),
                TimelineEvent("State Pharmacy Counseling & Seat Choice Locking", "July 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Radioactive Decay Law" to "N(t) = N₀ e^(-λt); Half-Life t₁/₂ = 0.693 / λ",
                "Poiseuille's Equation for Viscosity" to "Q = (π P r⁴) / (8 η L)",
                "pH of Buffer Solution (Henderson-Hasselbalch)" to "pH = pKa + log([Conjugate Base] / [Weak Acid])",
                "Hardy-Weinberg Genetic Equilibrium" to "p² + 2pq + q² = 1; p + q = 1"
            )
        )
    }

    private fun getCpetDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.GRADUATION_CPET,
            tier = "Degree & Lifelong Education",
            conductingBody = "Higher Education Department, Govt of Odisha / SAMS Odisha",
            shortCode = "Odisha CPET",
            fullTitle = "Odisha Common PG Entrance Test (CPET M.Sc / M.A / M.Com)",
            odiaTitle = "ଓଡ଼ିଶା ସାଧାରଣ ସ୍ନାତକୋତ୍ତର ପ୍ରବେଶିକା ପରୀକ୍ଷା (CPET PG)",
            tagLine = "Unified Gateway for Post-Graduate Degree Admissions across All Odisha Universities",
            examDurationMinutes = 90,
            totalQuestions = 80,
            totalMarks = 80,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "CBT / OMR Format",
            examLanguage = "English & Odia (for Odia Literature)",
            ageCriteria = "No upper age restriction for CPET post-graduate admissions",
            qualificationSummary = "Bachelor's degree with Honours / Core subject with at least 45% aggregate marks.",
            eligibilityRules = listOf(
                "Passed Bachelor's Degree examination in relevant subject discipline with minimum 45% marks (40% for SC/ST).",
                "Final year appearing undergraduate students are eligible to sit for CPET.",
                "Single application allows admission consideration across Utkal, Ravenshaw, Sambalpur, Berhampur, Fakir Mohan & Maharaja Sriram Chandra Bhanja Deo Universities.",
                "State-wide SAMS online unified counseling for seat allotment."
            ),
            subjects = listOf(
                SubjectWeightage("Core Subject Specialization", 50, 50, 62, listOf("Advanced Subject Theory", "Research Concepts", "Core University Syllabus")),
                SubjectWeightage("General Awareness & Analytical Reasoning", 15, 15, 19, listOf("Logical Deduction", "Current Scientific Trends", "Odisha Higher Education Policies")),
                SubjectWeightage("Quantitative & Data Interpretation", 15, 15, 19, listOf("Graphs & Charts", "Percentage & Ratios", "Statistical Averages"))
            ),
            syllabusModules = listOf(
                SyllabusModule("UG Core Subject In-Depth Theory", "Specialization", listOf("Advanced university syllabus modules", "Experimental methods and practical principles", "Key theorems and conceptual foundations"), 24, "CRITICAL"),
                SyllabusModule("Logical Deduction & Data Analysis", "Aptitude", listOf("Syllogisms and Venn diagrams", "Data tables, bar charts and pie graphs", "Series completion and coding-decoding"), 12, "HIGH"),
                SyllabusModule("General Academic Awareness", "GK", listOf("Higher education developments in Odisha", "Nobel prize discoveries & science updates", "Indian academic research bodies (UGC, CSIR, ICAR)"), 10, "MODERATE")
            ),
            patternSections = listOf(
                PatternSection("Core Subject Domain (50 Qs)", 50, 1, "0", 55),
                PatternSection("Analytical & General Aptitude (30 Qs)", 30, 1, "0", 35)
            ),
            cutoffStats = listOf(
                CutoffStat("Ravenshaw University (Top Subjects)", "64+ / 80", "70+ / 80"),
                CutoffStat("Utkal University (Vani Vihar)", "60+ / 80", "66+ / 80"),
                CutoffStat("Sambalpur University (Jyoti Vihar)", "52+ / 80", "58+ / 80"),
                CutoffStat("Berhampur University (Bhanja Bihar)", "48+ / 80", "55+ / 80")
            ),
            aiPredictions2026 = listOf(
                "CPET 2026 will test analytical problem-solving over direct rote memory definitions.",
                "Interdisciplinary application questions carry higher weightage across science and humanities streams.",
                "SAMS unified seat allocation favors candidates with strong consistency in both CPET score and career marks."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("CPET Entrance Examination", "June 2026", "Upcoming"),
                TimelineEvent("State-wide Subject Merit Rank Publication", "July 2026", "Upcoming"),
                TimelineEvent("SAMS PG University Choice Preference Locking", "July - August 2026", "Upcoming"),
                TimelineEvent("Phase 1 & Phase 2 University Seat Allotment", "August 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "CPET Career Mark Weightage" to "Total Score = (CPET Entrance Marks / 80 × 70) + Career Marks (30)",
                "Standard Deviation" to "σ = √[ Σ(x - μ)² / N ]",
                "Correlation Coefficient" to "r ranges from -1 to +1 indicating strength of linear association"
            )
        )
    }

    private fun getSchoolFoundationDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.SCHOOL_FOUNDATION,
            tier = "School & Talent Scholarships",
            conductingBody = "BSE Odisha / TE&SCERT / NCERT",
            shortCode = "Class 1-10",
            fullTitle = "Class 1-10 Talent Search, NRTS, NMMS & Board Foundation",
            odiaTitle = "୧ମ ରୁ ୧୦ମ ଶ୍ରେଣୀ ପ୍ରତିଭା ଅନ୍ୱେଷଣ (NRTS, NMMS & Board)",
            tagLine = "Early Childhood to High School STEM Foundation, Olympiad Preparation & Scholarship Exams",
            examDurationMinutes = 90,
            totalQuestions = 60,
            totalMarks = 60,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "Pen-Paper / Interactive CBT",
            examLanguage = "Odia & English",
            ageCriteria = "Ages 6 to 16 Years (Classes 1 through 10)",
            qualificationSummary = "Continuing study in recognized elementary and high schools across Odisha.",
            eligibilityRules = listOf(
                "NRTS (National Rural Talent Scholarship): Class 8 students studying in recognized schools in rural Odisha.",
                "NMMS (National Means-cum-Merit Scholarship): Class 8 students with family income less than ₹3.5 Lakh/year.",
                "Primary & Middle Talent Search: Class 3 and Class 5 students.",
                "High School Board Foundation: Class 9 and 10 students aiming for 90%+ in BSE Odisha Matriculation."
            ),
            subjects = listOf(
                SubjectWeightage("Science (Physics, Chemistry, Biology)", 20, 20, 33, listOf("Living World", "Force & Motion", "Matter & Energy", "Environmental Science")),
                SubjectWeightage("Mathematics", 20, 20, 33, listOf("Numbers & Operations", "Algebra Basics", "Geometry & Angles", "Data Handling")),
                SubjectWeightage("Language & Social Studies", 20, 20, 34, listOf("Odia Sahitya", "English Grammar", "Odisha History", "Civics & Geography"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Foundational Science & Inquiry", "Science", listOf("Properties of matter & mixtures", "Plant & animal life cycles", "Energy, work and heat transfer", "Earth systems & weather"), 14, "CRITICAL"),
                SyllabusModule("Mathematical Problem Solving", "Mathematics", listOf("Whole numbers, fractions and decimals", "Mensuration of perimeter and area", "Introduction to variables and equations", "Basic angles and triangles"), 14, "CRITICAL"),
                SyllabusModule("Language Arts & Reading Fluency", "Language", listOf("Reading comprehension passages", "Odia & English vocabulary building", "Sentence structure and punctuation", "Creative writing basics"), 12, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Science Section (20 Qs)", 20, 1, "0", 30),
                PatternSection("Mathematics Section (20 Qs)", 20, 1, "0", 30),
                PatternSection("Language & Social (20 Qs)", 20, 1, "0", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("NRTS Scholarship Winner Score", "52+ / 60", "56+ / 60"),
                CutoffStat("NMMS National Scholarship Cutoff", "45+ / 60", "50+ / 60")
            ),
            aiPredictions2026 = listOf(
                "Punyansu AI uses interactive visual stories and step-by-step illustrations for young learners.",
                "Scholarship exams emphasize arithmetic speed tricks and basic everyday science reasoning."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("NRTS & NMMS State Exam Window", "November 2026", "Upcoming"),
                TimelineEvent("Scholarship Result Declaration", "February 2027", "Upcoming")
            ),
            keyFormulas = listOf(
                "Area of Triangle" to "1/2 × Base × Height",
                "Speed Formula" to "Distance ÷ Time",
                "Plant Food Making" to "Photosynthesis uses sunlight, water, and carbon dioxide"
            )
        )
    }

    private fun getSecondaryChseDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.SECONDARY_CHSE,
            tier = "School & Talent Scholarships",
            conductingBody = "Council of Higher Secondary Education (CHSE), Odisha / CBSE",
            shortCode = "CHSE +2",
            fullTitle = "Class 11-12 CHSE / CBSE Board & Entrance Foundation",
            odiaTitle = "ଯୁକ୍ତ ଦୁଇ (+2) CHSE ଓ CBSE ବୋର୍ଡ଼ ପ୍ରସ୍ତୁତି",
            tagLine = "Physics, Chemistry, Math, Biology, Commerce & Arts Higher Secondary Curriculum",
            examDurationMinutes = 120,
            totalQuestions = 60,
            totalMarks = 100,
            markingScheme = "+1 to +4 based on question type",
            examMode = "Board Pattern & CBT Entrance Prep",
            examLanguage = "English & Odia",
            ageCriteria = "Ages 16 to 18 Years (Classes 11 & 12)",
            qualificationSummary = "Passed Class 10 Matriculation examination from BSE Odisha, CBSE or equivalent.",
            eligibilityRules = listOf(
                "Enrolled in Class 11 or 12 Higher Secondary School / Junior College in Odisha.",
                "Covers both Annual CHSE Board pattern and competitive entrance foundation.",
                "Practical laboratory experiment guides included."
            ),
            subjects = listOf(
                SubjectWeightage("Physics (+2 Science)", 25, 25, 33, listOf("Mechanics", "Thermodynamics", "Wave Optics", "Semiconductors")),
                SubjectWeightage("Chemistry (+2 Science)", 25, 25, 33, listOf("Physical Chemistry", "Organic Reactions", "Coordination Chemistry")),
                SubjectWeightage("Mathematics / Biology", 25, 25, 34, listOf("Calculus", "Vectors", "Genetics", "Physiology"))
            ),
            syllabusModules = listOf(
                SyllabusModule("+2 Core Physics Mastery", "Physics", listOf("Laws of motion & gravitation", "Oscillations & waves", "Electrostatics & magnetism", "Modern physics"), 20, "CRITICAL"),
                SyllabusModule("+2 Core Chemistry Fundamentals", "Chemistry", listOf("Atomic structure & periodic properties", "Chemical thermodynamics", "Hydrocarbons & organic mechanisms", "Electrochemistry"), 20, "CRITICAL"),
                SyllabusModule("+2 Higher Mathematics", "Mathematics", listOf("Limits, derivatives and integrals", "Trigonometric inverse functions", "Coordinate 3D geometry", "Probability distributions"), 20, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Theory Section (MCQs & Short Answer)", 40, 1, "0", 60),
                PatternSection("Long Answer Derivation & Numerical Section", 20, 3, "0", 60)
            ),
            cutoffStats = listOf(
                CutoffStat("CHSE Distinction Grade (75%+)", "75 / 100", "85+ / 100"),
                CutoffStat("First Division (60%+)", "60 / 100", "70+ / 100")
            ),
            aiPredictions2026 = listOf(
                "CHSE 2026 will maintain 50% objective MCQ format following latest Council guidelines.",
                "Derivations in Electrostatics (Gauss Law) and Organic synthesis conversions are high probability."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("CHSE Annual Board Exams", "February - March 2026", "Upcoming"),
                TimelineEvent("CHSE Odisha Result Declaration", "May 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Coulomb's Law" to "F = (1 / 4πε₀) × (q₁q₂ / r²)",
                "Ideal Gas Law" to "PV = nRT",
                "Derivative of sin(x)" to "d/dx [sin(x)] = cos(x)"
            )
        )
    }

    private fun getOjeeAllDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.OJEE_ALL,
            tier = "Professional & Technical Entrance",
            conductingBody = "Odisha Joint Entrance Examination (OJEE) Board",
            shortCode = "OJEE All",
            fullTitle = "OJEE All Integrated Streams (B.Tech, B.Pharm, MCA, MBA, M.Tech)",
            odiaTitle = "OJEE ସମସ୍ତ ବୈଷୟିକ ଓ ପ୍ରଫେସନାଲ୍ ପ୍ରବେଶିକା",
            tagLine = "Complete Unified Entrance Suite for All Under-Graduate & Post-Graduate Professional Courses",
            examDurationMinutes = 120,
            totalQuestions = 120,
            totalMarks = 480,
            markingScheme = "+4 Marks for Correct, -1 Mark for Wrong",
            examMode = "Computer-Based Test (CBT)",
            examLanguage = "English",
            ageCriteria = "Ages 17 to 35 Years based on chosen professional course",
            qualificationSummary = "10+2 / Diploma / Bachelor's Degree depending on B.Tech / MCA / MBA / M.Tech track.",
            eligibilityRules = listOf(
                "Single gateway application for all degree & PG technical programs across Odisha.",
                "Admissions strictly based on state rank and centralized web counseling.",
                "State domicile reservations apply for government colleges."
            ),
            subjects = listOf(
                SubjectWeightage("Quantitative Aptitude", 30, 120, 25, listOf("Arithmetic", "Algebra", "Geometry", "Modern Math")),
                SubjectWeightage("Logical & Analytical Reasoning", 30, 120, 25, listOf("Puzzles", "Critical Reasoning", "Data Sufficiency")),
                SubjectWeightage("Verbal Ability & Reading Comprehension", 30, 120, 25, listOf("Grammar", "Vocabulary", "Passages")),
                SubjectWeightage("Core Domain / Computer Awareness", 30, 120, 25, listOf("Basic Computing", "Science / Management Principles"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Advanced Quantitative & Logical Aptitude", "Aptitude", listOf("Number properties and algebra", "Data interpretation charts", "Analytical reasoning puzzles"), 18, "CRITICAL"),
                SyllabusModule("Verbal Proficiency & Business Communication", "Language", listOf("Advanced comprehension", "Error spotting and sentence correction", "Idioms and phrases"), 14, "HIGH"),
                SyllabusModule("Computer Fundamentals & Domain Concepts", "Domain", listOf("Operating systems & data structures", "Database fundamentals", "Basic networking"), 16, "HIGH")
            ),
            patternSections = listOf(
                PatternSection("Quantitative Aptitude (30 Qs)", 30, 4, "-1 Mark", 30),
                PatternSection("Logical Reasoning (30 Qs)", 30, 4, "-1 Mark", 30),
                PatternSection("Verbal Ability (30 Qs)", 30, 4, "-1 Mark", 30),
                PatternSection("Domain Knowledge (30 Qs)", 30, 4, "-1 Mark", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("Top Govt MCA / MBA (CET / Utkal)", "340+ / 480", "380+ / 480"),
                CutoffStat("Top Private Professional Colleges", "180+ / 480", "220+ / 480")
            ),
            aiPredictions2026 = listOf(
                "Punyansu AI dynamically adapts question difficulty based on student's chosen specialization.",
                "High probability of data interpretation questions involving compound growth and line graphs."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("OJEE All-Stream CBT Window", "May 2026", "Upcoming"),
                TimelineEvent("Centralized Seat Allotment", "July 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Compound Interest Formula" to "A = P(1 + r/n)^(nt)",
                "Combinations Formula" to "nCr = n! / [r! (n - r)!]"
            )
        )
    }

    private fun getOdishaAllGovtDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.ODISHA_ALL_ENTRANCE,
            tier = "Civil Services & State Govt",
            conductingBody = "OSSC, OSSSC & Odisha State Recruitment Commissions",
            shortCode = "Odisha All Govt",
            fullTitle = "Odisha All Govt & Technical Recruitment Entrances (OSSC, OSSSC)",
            odiaTitle = "ଓଡ଼ିଶା ସମସ୍ତ ସରକାରୀ ଓ ବୈଷୟିକ ନିଯୁକ୍ତି ପ୍ରବେଶିକା",
            tagLine = "Combined Preparation for RI, AMIN, Junior Clerk, Forest Guard, Police & Technical Posts",
            examDurationMinutes = 120,
            totalQuestions = 100,
            totalMarks = 100,
            markingScheme = "+1 Mark for Correct, -0.25 Mark for Wrong",
            examMode = "CBT / OMR Objective Exam",
            examLanguage = "Odia & English",
            ageCriteria = "Ages 18 to 38 Years (Up to 43 Years for Reserved Categories)",
            qualificationSummary = "10th / +2 / Bachelor's Degree according to specific state department notification.",
            eligibilityRules = listOf(
                "Citizen of India with permanent residency in Odisha.",
                "Must be able to speak, read and write Odia and passed HSC with Odia language.",
                "Age relaxations as per Odisha Govt reservation rules."
            ),
            subjects = listOf(
                SubjectWeightage("Odisha General Knowledge & History", 25, 25, 25, listOf("Odisha Geography", "Freedom Movement", "Culture & Temples", "Current Affairs")),
                SubjectWeightage("Arithmetic & Mathematics", 25, 25, 25, listOf("Percentage", "Profit & Loss", "Time & Work", "Mensuration")),
                SubjectWeightage("General English & Odia Grammar", 25, 25, 25, listOf("English Grammar", "Odia Grammar", "Comprehension", "Vocabulary")),
                SubjectWeightage("Logical Reasoning & Computer Skills", 25, 25, 25, listOf("Analogy", "Series", "MS Office", "Internet Basics"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Comprehensive Odisha General Studies", "Odisha GK", listOf("Districts and rivers of Odisha", "Odisha government schemes (Mo Seva Kendra, LAccMI)", "Odisha literature and authors"), 18, "CRITICAL"),
                SyllabusModule("Competitive Arithmetic & Shortcuts", "Arithmetic", listOf("Unitary method, ratio and proportion", "Time, speed, distance and work", "Simple and compound interest", "Square root and cube root tricks"), 18, "CRITICAL"),
                SyllabusModule("Odia & English Language Competency", "Language", listOf("Odia spelling rules & Sandhi", "Idioms and proverbs (ଢଗ ଢମାଳି)", "English active-passive voice & prepositions"), 16, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Odisha GK & Current Affairs (25 Qs)", 25, 1, "-0.25 Mark", 30),
                PatternSection("Arithmetic (25 Qs)", 25, 1, "-0.25 Mark", 30),
                PatternSection("English & Odia (25 Qs)", 25, 1, "-0.25 Mark", 30),
                PatternSection("Reasoning & Computer (25 Qs)", 25, 1, "-0.25 Mark", 30)
            ),
            cutoffStats = listOf(
                CutoffStat("UR / General Category Expected Cutoff", "74 - 82 / 100", "85+ / 100"),
                CutoffStat("SEBC Category", "70 - 76 / 100", "80+ / 100"),
                CutoffStat("SC / ST Category", "60 - 68 / 100", "72+ / 100")
            ),
            aiPredictions2026 = listOf(
                "Odisha GK will feature new initiatives, ports (Paradip, Dhamra, Gopalpur) and district-wise GI tags (Kandhamal Haldi, Rasagola, Kotpad Handloom).",
                "Computer section focuses heavily on keyboard shortcuts and cybersecurity terms."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("Combined State Examination Window", "Rolling Throughout 2026", "Upcoming"),
                TimelineEvent("Certificate Verification & Skill Test", "Following Written Test", "Upcoming")
            ),
            keyFormulas = listOf(
                "Odisha Formation Date" to "1 April 1936 (Utkal Divas)",
                "State Animal & Bird of Odisha" to "Animal: Sambar deer; Bird: Indian Roller (Neelkanth)",
                "Time and Work" to "If A does work in X days and B in Y days, together in (XY) / (X + Y) days"
            )
        )
    }

    private fun getItiPolytechnicDetails(): ExamCategoryDetail {
        return ExamCategoryDetail(
            stream = ExamStream.ITI_POLYTECHNIC,
            tier = "Professional & Technical Entrance",
            conductingBody = "Directorate of Technical Education & Training (DTET), Odisha",
            shortCode = "Odisha ITI",
            fullTitle = "Odisha ITI & Trade Polytechnic Admissions",
            odiaTitle = "ଓଡ଼ିଶା ଆଇଟିଆଇ (ITI) ଓ ବୈଷୟିକ ପ୍ରଶିକ୍ଷଣ",
            tagLine = "Skill India Practical Trades: Electrician, Fitter, Welder, COPA & Turner",
            examDurationMinutes = 60,
            totalQuestions = 50,
            totalMarks = 50,
            markingScheme = "+1 Mark for Correct, No Negative Marking",
            examMode = "CBT / Merit Admission",
            examLanguage = "Odia & English",
            ageCriteria = "Minimum 14 Years. No upper age limit for vocational trades",
            qualificationSummary = "Passed Class 8 / Class 10 (HSC) from a recognized board.",
            eligibilityRules = listOf(
                "Passed Class 8 or Class 10 according to specific trade requirements (Electrician/Fitter requires 10th with Science/Math).",
                "Direct admission and counseling via SAMS Odisha ITI portal.",
                "Stipend and apprenticeship support under Odisha Skill Development Authority (OSDA)."
            ),
            subjects = listOf(
                SubjectWeightage("Workshop Science & Calculation", 20, 20, 40, listOf("Units & Measurements", "Work, Power & Energy", "Heat & Temperature", "Basic Electricity")),
                SubjectWeightage("Trade Theory & Safety", 15, 15, 30, listOf("Workshop Safety", "Hand Tools", "Measuring Instruments", "Fire Safety")),
                SubjectWeightage("General Aptitude & Odia/English", 15, 15, 30, listOf("Numerical Ability", "Basic English Terms", "Odia Comprehension"))
            ),
            syllabusModules = listOf(
                SyllabusModule("Basic Electrical & Mechanical Science", "Trade Science", listOf("SI units and conversions", "Speed, velocity and acceleration", "Mass, density and specific gravity", "Conductors, insulators and Ohm's law"), 12, "CRITICAL"),
                SyllabusModule("Workshop Safety & Hand Tool Precision", "Safety", listOf("First aid and PPE kit usage", "Vernier caliper and micrometer reading", "Types of files, chisels and hacksaws", "Electrical hazard precautions"), 12, "CRITICAL")
            ),
            patternSections = listOf(
                PatternSection("Workshop Science & Calculation (20 Qs)", 20, 1, "0", 25),
                PatternSection("Trade Theory & Safety (15 Qs)", 15, 1, "0", 20),
                PatternSection("General Aptitude (15 Qs)", 15, 1, "0", 15)
            ),
            cutoffStats = listOf(
                CutoffStat("Govt ITI Cuttack / Rourkela (Electrician)", "80%+ Class 10 Merit", "85%+"),
                CutoffStat("Govt ITI Berhampur (Fitter)", "75%+ Class 10 Merit", "80%+")
            ),
            aiPredictions2026 = listOf(
                "Questions will focus on Vernier caliper least count (0.02 mm) and micrometer screw gauge calculations.",
                "Electrical section targets Color Coding of Resistors and Fleming's Right Hand Rule."
            ),
            counselingRoadmap = listOf(
                TimelineEvent("SAMS Odisha ITI Online Application", "June 2026", "Upcoming"),
                TimelineEvent("Trade Choice Locking & Seat Allotment", "July 2026", "Upcoming")
            ),
            keyFormulas = listOf(
                "Vernier Least Count" to "Least Count = 1 Main Scale Division - 1 Vernier Scale Division = 0.02 mm",
                "Work Done" to "Work = Force × Displacement (Joules)",
                "Power Formula" to "Power = Work / Time (Watts)"
            )
        )
    }
}
