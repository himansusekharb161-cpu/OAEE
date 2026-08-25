package com.example.data.repository

import com.example.data.model.DailyStudyTip
import com.example.data.model.ExamStream
import java.util.Calendar

object DailyStudyTipsProvider {

    private val allTips: List<DailyStudyTip> = listOf(
        DailyStudyTip(
            id = "tip_ct_01",
            title = "Odia Grammar: Sandhi vs Samasa Differentiation",
            category = "CT & Pedagogy",
            examTarget = "Odisha CT (D.El.Ed) & OTET",
            englishAdvice = "In SCERT CT exams, questions on Swara Sandhi vs Byanjana Sandhi are frequent. Remember: Sandhi joins two sounds/letters (ବର୍ଣ୍ଣର ମିଳନ), while Samasa joins two meaningful words (ପଦର ମିଳନ). Look for root stems first before answering.",
            odiaAdvice = "ସନ୍ଧି ଓ ସମାସ ମଧ୍ୟରେ ପାର୍ଥକ୍ୟ ମନେରଖନ୍ତୁ: ଦୁଇଟି ବର୍ଣ୍ଣର ମିଳନକୁ 'ସନ୍ଧି' ଏବଂ ଏକାଧିକ ପଦର ଏକପଦୀକରଣକୁ 'ସମାସ' କୁହାଯାଏ। ପରୀକ୍ଷାରେ ପ୍ରଥମେ ମୂଳ ଶବ୍ଦ ଚିହ୍ନଟ କରନ୍ତୁ।",
            actionableRule = "Solve at least 15 Sandhi-Bichheda questions daily to guarantee full marks in Odia section.",
            targetStream = ExamStream.CT_ENTRANCE,
            tags = listOf("CT", "Odia", "Grammar", "Sandhi", "Pedagogy")
        ),
        DailyStudyTip(
            id = "tip_ojee_01",
            title = "OJEE CBT 54-Second Pacing Rule",
            category = "OJEE CBT Tactics",
            examTarget = "OJEE B.Tech & Lateral Entry",
            englishAdvice = "OJEE has 120 questions in 120 minutes with +4 / -1 marking. Allocate maximum 54 seconds per Physics/Math question. If a numerical takes > 60 seconds to set up, flag it for Review and move ahead immediately.",
            odiaAdvice = "OJEE ପରୀକ୍ଷାରେ ୧୨୦ ପ୍ରଶ୍ନ ପାଇଁ ୧୨୦ ମିନିଟ୍ ମିଳେ। ପ୍ରତ୍ୟେକ ପ୍ରଶ୍ନ ପାଇଁ ସର୍ବାଧିକ ୫୪ ସେକେଣ୍ଡ ବିନିଯୋଗ କରନ୍ତୁ। ଜଟିଳ ଗାଣିତିକ ପ୍ରଶ୍ନକୁ Review ପାଇଁ ରଖି ଆଗକୁ ବଢ଼ନ୍ତୁ।",
            actionableRule = "Use the 3-Pass strategy: Pass 1 for direct theory, Pass 2 for standard numericals, Pass 3 for lengthy calculations.",
            targetStream = ExamStream.ENGINEERING,
            tags = listOf("OJEE", "B.Tech", "CBT", "Time Management", "Physics")
        ),
        DailyStudyTip(
            id = "tip_navodaya_01",
            title = "Mental Ability: Mirror & Embedded Figures Shortcut",
            category = "Navodaya & Math",
            examTarget = "JNVST Navodaya (Class 6 & 9)",
            englishAdvice = "In JNVST Mental Ability (MAT), never guess rotated mirror images mentally. Rotate your rough scrap paper 90 or 180 degrees or track key asymmetrical corners (dots/arrows) to eliminate 2 wrong options instantly.",
            odiaAdvice = "ଜବାହର ନବୋଦୟ ମାନସିକ ଦକ୍ଷତା (MAT) ରେ ଆଇନା ପ୍ରତିବିମ୍ବ ପ୍ରଶ୍ନରେ କୋଣ ଓ ତୀର ଚିହ୍ନକୁ ଲକ୍ଷ୍ୟ କରି ପ୍ରଥମେ ଭୁଲ ବିକଳ୍ପଗୁଡ଼ିକୁ ବାଦ୍ (Eliminate) କରନ୍ତୁ।",
            actionableRule = "Option elimination gives 95%+ accuracy in non-verbal reasoning within 25 seconds.",
            targetStream = ExamStream.NAVODAYA_ENTRANCE,
            tags = listOf("JNVST", "Navodaya", "Mental Ability", "Class 6", "Reasoning")
        ),
        DailyStudyTip(
            id = "tip_pathani_01",
            title = "Pathani Samanta: Prime Modulo & Last Digit Tricks",
            category = "Math & Talent",
            examTarget = "Pathani Samanta Mathematics (PSMSE)",
            englishAdvice = "PSMSE Stage I & II heavily test cyclicity of exponents and unit digits (e.g. 7^2026 mod 10). Remember that base numbers 2, 3, 7, 8 repeat their units digit in cycles of 4. Divide power by 4 and use the remainder!",
            odiaAdvice = "ପଠାଣି ସାମନ୍ତ ଗଣିତ ପରୀକ୍ଷା ପାଇଁ ଏକକ ସ୍ଥାନୀୟ ଅଙ୍କ ନିର୍ଣ୍ଣୟ (Unit Digit Cyclicity) ନିୟମ ଶିଖନ୍ତୁ। ୨, ୩, ୭, ୮ ର ଘାତ ପ୍ରତି ୪ ଥରରେ ପୁନରାବୃତ୍ତି ହୁଏ।",
            actionableRule = "Master Euler Totient and Unit Digit cyclicity to solve 3-mark Olympiad math questions in 40s.",
            targetStream = ExamStream.PATHANI_SAMANTA,
            tags = listOf("PSMSE", "Pathani Samanta", "Mathematics", "Number Theory", "Olympiad")
        ),
        DailyStudyTip(
            id = "tip_oas_01",
            title = "OPSC OAS: Odisha River Systems & Major Dams",
            category = "OAS & State GK",
            examTarget = "OPSC OAS Civil Services",
            englishAdvice = "Odisha Geography in Prelims Paper 1 consistently asks about Mahanadi tributaries (Ib, Ong, Tel, Jonk) and Rushikulya/Baitarani origins. Memorize left vs right bank tributaries using an east-flowing sketch map.",
            odiaAdvice = "ଓଡ଼ିଶା ପ୍ରଶାସନିକ ସେବା (OAS) ପାଇଁ ମହାନଦୀର ଉପନଦୀ (ଇବ୍, ତେଲ୍, ଅଙ୍ଗ୍, ଜୋଙ୍କ) ଓ ବହୁମୁଖୀ ନଦୀବନ୍ଧ ଯୋଜନାଗୁଡ଼ିକୁ ମାନଚିତ୍ର ସାହାଯ୍ୟରେ ଅଭ୍ୟାସ କରନ୍ତୁ।",
            actionableRule = "Draw an outline map of Odisha every Sunday and locate 5 national parks, rivers, and mineral belts.",
            targetStream = ExamStream.OAS_IAS_CIVIL,
            tags = listOf("OAS", "OPSC", "Odisha GK", "Geography", "Prelims")
        ),
        DailyStudyTip(
            id = "tip_det_01",
            title = "Diploma DET: Dimensional Analysis for Quick Verification",
            category = "DET & Polytechnic",
            examTarget = "Odisha DET & Polytechnic Admissions",
            englishAdvice = "In Engineering Physics and Mechanics for Odisha DET, if you forget a formula for work, power, or torque, apply dimensional analysis [M L^2 T^-2]. It verifies units across choices in under 15 seconds.",
            odiaAdvice = "ଡିପ୍ଲୋମା DET ପରୀକ୍ଷାରେ ଭୌତିକ ବିଜ୍ଞାନର ସୂତ୍ର ଭୁଲିଗଲେ 'ପାରିମାଣିକ ବିଶ୍ଳେଷଣ' (Dimensional Analysis) ପ୍ରୟୋଗ କରି ସଠିକ୍ ଉତ୍ତର ନିର୍ଣ୍ଣୟ କରନ୍ତୁ।",
            actionableRule = "Keep a 1-page formula sheet of SI Units and Dimensions on your study desk.",
            targetStream = ExamStream.DIPLOMA_DET,
            tags = listOf("DET", "Polytechnic", "Physics", "Mechanics", "Engineering")
        ),
        DailyStudyTip(
            id = "tip_otet_01",
            title = "Child Development: Piaget vs Vygotsky Key Distinctions",
            category = "CT & Pedagogy",
            examTarget = "OTET & CT D.El.Ed",
            englishAdvice = "In Child Development & Pedagogy (CDP), Piaget emphasizes cognitive stages and individual constructivism, whereas Vygotsky emphasizes Socio-Cultural Context, More Knowledgeable Other (MKO), and Zone of Proximal Development (ZPD).",
            odiaAdvice = "ଶିଶୁ ବିକାଶ ଓ ଶିକ୍ଷାଦାନ ପଦ୍ଧତି (CDP) ରେ ପିଆଜେଟ୍ ବ୍ୟକ୍ତିଗତ ଜ୍ଞାନ ନିର୍ମାଣ ଉପରେ ଏବଂ ଭାଇଗୋଟ୍ସ୍କି ସାମାଜିକ ଓ ସାଂସ୍କୃତିକ ପରିବେଶ (ZPD / Scaffolding) ଉପରେ ଗୁରୁତ୍ୱ ଦିଅନ୍ତି।",
            actionableRule = "Whenever a question mentions 'peer collaboration' or 'scaffolding', think Vygotsky first.",
            targetStream = ExamStream.TEACHER_OTET,
            tags = listOf("OTET", "CDP", "Pedagogy", "Piaget", "Vygotsky")
        ),
        DailyStudyTip(
            id = "tip_strategy_01",
            title = "Negative Marking Defense: The 50-50 Probability Threshold",
            category = "Exam Strategy",
            examTarget = "All Odisha Competitive Exams (OJEE, CT, OAS)",
            englishAdvice = "If an exam has -0.25 negative penalty: If you can eliminate 2 out of 4 options with certainty, statistically attempting the question yields +0.375 expected value. However, if all 4 options are unknown, NEVER blind guess!",
            odiaAdvice = "ନେଗେଟିଭ୍ ମାର୍କିଂ ଥିବା ପରୀକ୍ଷାରେ ଯଦି ଆପଣ ୨ଟି ବିକଳ୍ପ ନିଶ୍ଚିତ ଭାବେ ବାଦ୍ ଦେଇପାରିବେ, ତେବେ ଉତ୍ତର ଦେବା ଲାଭଦାୟକ। କିନ୍ତୁ ଅଜଣା ପ୍ରଶ୍ନରେ କେବେହେଲେ ଅନ୍ଧ ଅନୁମାନ କରନ୍ତୁ ନାହିଁ।",
            actionableRule = "Mark questionable items with '50-50 Flag' during CBT and review only if time permits.",
            targetStream = null,
            tags = listOf("Strategy", "Negative Marking", "Accuracy", "Mock Test")
        ),
        DailyStudyTip(
            id = "tip_strategy_02",
            title = "Active Recall & Spaced Repetition (20-Minute Daily Audit)",
            category = "Memory & Habits",
            examTarget = "All Odisha Entrance Aspirants",
            englishAdvice = "Passive re-reading of textbooks only creates the illusion of competence. Spend the last 20 minutes of your study session closing the book and writing key formulas, rules, and definitions from pure memory on blank paper.",
            odiaAdvice = "କେବଳ ବହି ପଢ଼ିବା ଅପେକ୍ଷା 'ଆକ୍ଟିଭ୍ ରିକଲ୍' (Active Recall) ପଦ୍ଧତିରେ ପଢ଼ା ଶେଷରେ ଖାତା ବନ୍ଦ କରି ମୁଖ୍ୟ ସୂତ୍ର ଓ ତଥ୍ୟଗୁଡ଼ିକୁ ମନରୁ ଲେଖି ଅଭ୍ୟାସ କରନ୍ତୁ।",
            actionableRule = "Do a 3-Day Spaced Repetition check: revise topics on Day 1, Day 3, and Day 7.",
            targetStream = null,
            tags = listOf("Memory", "Active Recall", "Spaced Repetition", "Habits")
        ),
        DailyStudyTip(
            id = "tip_oav_01",
            title = "OAV Entrance: English Grammar Preposition Collocations",
            category = "OAV & School Foundation",
            examTarget = "Odisha Adarsha Vidyalaya (OAVS Class 6)",
            englishAdvice = "OAV Class 6 Entrance tests English prepositions strictly through collocations (e.g. 'fond of', 'good at', 'listen to', 'congratulate on'). Learn prepositions attached to verbs rather than isolated rules.",
            odiaAdvice = "ଓଡ଼ିଶା ଆଦର୍ଶ ବିଦ୍ୟାଳୟ (OAV) ପ୍ରବେଶିକା ପରୀକ୍ଷା ପାଇଁ ଇଂରାଜୀ Prepositions କୁ ସଠିକ୍ କ୍ରିୟାପଦ (Verb Collocation) ସହିତ ଯୋଡ଼ି ମନେରଖନ୍ତୁ।",
            actionableRule = "Practice 20 sentence fill-ups targeting common phrasal prepositions weekly.",
            targetStream = ExamStream.OAV_ENTRANCE,
            tags = listOf("OAV", "Class 6", "English", "Grammar", "Prepositions")
        ),
        DailyStudyTip(
            id = "tip_osou_01",
            title = "Adult & Lifelong Education: The 25-Minute Pomodoro Rhythm",
            category = "Lifelong Learning",
            examTarget = "OSOU Adult & Continuing Education (Up to 55+ Yrs)",
            englishAdvice = "Balancing family, work, and studies? You don't need 8 continuous hours. Use two 25-minute focused Pomodoro blocks in the early morning and two in the evening. Consistent 50 mins beats irregular marathon study.",
            odiaAdvice = "କାର୍ଯ୍ୟବ୍ୟସ୍ତତା ମଧ୍ୟରେ ପଢ଼ିବା ପାଇଁ 'ପୋମୋଡୋରୋ' (୨୫ ମିନିଟ୍ ଏକାଗ୍ରତା + ୫ ମିନିଟ୍ ବିଶ୍ରାମ) ପଦ୍ଧତି ଆପଣାନ୍ତୁ। ନିୟମିତ ୫୦ ମିନିଟ୍ ଅଭ୍ୟାସ ସଫଳତା ଦିଏ।",
            actionableRule = "Keep a single pocket notebook for tricky terms and review 5 items during transit or tea breaks.",
            targetStream = ExamStream.ADULT_CONTINUING_ED,
            tags = listOf("OSOU", "Adult Ed", "Lifelong Learning", "Pomodoro", "Time Management")
        ),
        DailyStudyTip(
            id = "tip_cpet_01",
            title = "CPET Post-Graduation: Previous 5-Year Question Trend Analysis",
            category = "Higher Education",
            examTarget = "Odisha CPET (State PG Entrance)",
            englishAdvice = "70% of Odisha CPET PG questions derive from Utkal & Ravenshaw University Core CBCS syllabus topics. Identify the top 5 high-yield units with recurring questions from the last 3 admission cycles first.",
            odiaAdvice = "ଓଡ଼ିଶା CPET ପିଜି ପ୍ରବେଶିକା ପରୀକ୍ଷାରେ ୭୦% ପ୍ରଶ୍ନ CBCS ଅନର୍ସ ସିଲାବସ୍ରୁ ଆସିଥାଏ। ପୂର୍ବ ବର୍ଷର ପ୍ରଶ୍ନୋତ୍ତର ବିଶ୍ଳେଷଣ କରି ମୁଖ୍ୟ ୟୁନିଟ୍ ଗୁଡ଼ିକୁ ପ୍ରାଥମିକତା ଦିଅନ୍ତୁ।",
            actionableRule = "Create a 2-page summary sheet for each of your Major PG core papers.",
            targetStream = ExamStream.GRADUATION_CPET,
            tags = listOf("CPET", "PG Entrance", "Ravenshaw", "Utkal", "Degree")
        ),
        DailyStudyTip(
            id = "tip_medical_01",
            title = "Pharmacy & Medical: Drug Mechanism & Organic Functional Groups",
            category = "Medical & Pharma",
            examTarget = "OJEE B.Pharm / Paramedical",
            englishAdvice = "In OJEE Pharmacy chemistry, focus on IUPAC nomenclature of heterocyclic rings and ester/ether reaction mechanisms. In Biology, human physiology and endocrine hormones carry nearly 35% of the total score.",
            odiaAdvice = "ଫାର୍ମାସୀ ଓ ପାରାମେଡିକାଲ ପ୍ରବେଶିକା ପାଇଁ ଜୈବ ରସାୟନର IUPAC ନାମକରଣ ଏବଂ ଜୀବବିଜ୍ଞାନରେ ଏଣ୍ଡୋକ୍ରାଇନ୍ ହରମୋନ୍ କାର୍ଯ୍ୟପ୍ରଣାଳୀ ଉପରେ ବିଶେଷ ଧ୍ୟାନ ଦିଅନ୍ତୁ।",
            actionableRule = "Review endocrine hormone charts and IUPAC ring structures every alternate morning.",
            targetStream = ExamStream.MEDICAL_PHARMA,
            tags = listOf("OJEE", "Pharmacy", "Chemistry", "Biology", "Medical")
        ),
        DailyStudyTip(
            id = "tip_cbt_02",
            title = "CBT Exam Screen Management & Palette Legend Mastery",
            category = "CBT Speed Tactics",
            examTarget = "All Online CBT Odisha Exams",
            englishAdvice = "Never waste time re-reading questions you marked as 'Green (Answered)'. Use 'Purple / Mark for Review' ONLY when you calculated the answer but want a quick sanity check before final submit.",
            odiaAdvice = "ଅନଲାଇନ୍ CBT ପରୀକ୍ଷାରେ Question Palette ର ରଙ୍ଗ ସଂକେତ ବୁଝନ୍ତୁ। ସବୁଜ (Green) ଚିହ୍ନିତ ଉତ୍ତରକୁ ବାରମ୍ବାର ପଢ଼ି ସମୟ ନଷ୍ଟ ନକରି କେବଳ 'Mark for Review' ପ୍ରଶ୍ନ ଯାଞ୍ଚ କରନ୍ତୁ।",
            actionableRule = "Spend the first 60 seconds of any mock test getting familiar with the question palette shortcut keys.",
            targetStream = null,
            tags = listOf("CBT", "Online Exam", "Test Tactics", "Computer Based")
        )
    )

    fun getAllTips(): List<DailyStudyTip> = allTips

    fun getTipsForStream(stream: ExamStream?): List<DailyStudyTip> {
        if (stream == null) return allTips
        val specific = allTips.filter { it.targetStream == stream }
        val general = allTips.filter { it.targetStream == null }
        return if (specific.isNotEmpty()) specific + general else allTips
    }

    fun getTipOfTheDay(stream: ExamStream? = null): DailyStudyTip {
        val streamTips = if (stream != null) {
            val matched = allTips.filter { it.targetStream == stream }
            if (matched.isNotEmpty()) matched else allTips
        } else {
            allTips
        }

        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear + (stream?.ordinal ?: 0)) % streamTips.size
        return streamTips[index]
    }

    fun getTipById(id: String): DailyStudyTip? {
        return allTips.find { it.id == id }
    }

    fun searchTips(query: String): List<DailyStudyTip> {
        if (query.isBlank()) return allTips
        val q = query.trim().lowercase()
        return allTips.filter {
            it.title.lowercase().contains(q) ||
            it.englishAdvice.lowercase().contains(q) ||
            it.odiaAdvice.lowercase().contains(q) ||
            it.category.lowercase().contains(q) ||
            it.examTarget.lowercase().contains(q) ||
            it.tags.any { tag -> tag.lowercase().contains(q) }
        }
    }
}
