package com.example.data.repository

import com.example.data.model.ExamStream
import com.example.data.model.Flashcard
import com.example.data.model.MockTest
import com.example.data.model.Question
import com.example.data.model.StudyInfoTopic

class ExamRepository {

    fun getMockTestsForStream(stream: ExamStream): List<MockTest> {
        return when (stream) {
            ExamStream.SCHOOL_FOUNDATION -> listOf(createSchoolFoundationMockTest())
            ExamStream.NAVODAYA_ENTRANCE -> listOf(createNavodayaMockTest())
            ExamStream.PATHANI_SAMANTA -> listOf(createPathaniSamantaMockTest())
            ExamStream.OAV_ENTRANCE -> listOf(createOavMockTest())
            ExamStream.SECONDARY_CHSE -> listOf(createChseSecondaryMockTest())
            ExamStream.CT_ENTRANCE -> listOf(createCtEntranceMockTest())
            ExamStream.ENGINEERING -> listOf(
                createEngineeringMockTest1(),
                createEngineeringFutureMockTest()
            )
            ExamStream.OJEE_ALL -> listOf(createOjeeAllMockTest())
            ExamStream.DIPLOMA_DET -> listOf(
                createDiplomaMockTest1(),
                createDiplomaFutureMockTest()
            )
            ExamStream.MEDICAL_PHARMA -> listOf(createMedicalMockTest1())
            ExamStream.GRADUATION_CPET -> listOf(createGraduationCpetMockTest())
            ExamStream.TEACHER_OTET -> listOf(createBedCpetMockTest1())
            ExamStream.OAS_IAS_CIVIL -> listOf(createOasIasCivilMockTest())
            ExamStream.ADULT_CONTINUING_ED -> listOf(createAdultOsouMockTest())
            ExamStream.ODISHA_ALL_ENTRANCE -> listOf(createOdishaAllGovtMockTest())
            ExamStream.ITI_POLYTECHNIC -> listOf(createItiPolytechnicMockTest1())
        }
    }

    fun getAllPyqQuestions(): List<Question> {
        return listOf(
            Question(
                id = "pyq_01",
                subject = "Physics",
                questionText = "A body of mass 2 kg is moving with a velocity of 10 m/s. What is its kinetic energy?",
                options = listOf("50 J", "100 J", "200 J", "400 J"),
                correctOptionIndex = 1,
                explanation = "Kinetic Energy KE = 1/2 * m * v^2 = 0.5 * 2 * (10)^2 = 100 Joules.",
                tag = "PAST 2024",
                difficulty = "Easy"
            ),
            Question(
                id = "pyq_02",
                subject = "Chemistry",
                questionText = "Which element has the highest electron affinity in the periodic table?",
                options = listOf("Fluorine", "Chlorine", "Bromine", "Oxygen"),
                correctOptionIndex = 1,
                explanation = "Chlorine (Cl) has the highest electron affinity because Fluorine has strong inter-electronic repulsions in its small 2p orbital.",
                tag = "PAST 2023",
                difficulty = "Medium"
            ),
            Question(
                id = "pyq_03",
                subject = "Mathematics",
                questionText = "Find the value of limit x -> 0 for (sin x) / x.",
                options = listOf("0", "1", "Infinity", "Undefined"),
                correctOptionIndex = 1,
                explanation = "Standard trigonometric limit: lim (x->0) (sin x / x) = 1 (proved via L'Hopital's Rule or Sandwich Theorem).",
                tag = "PAST 2022",
                difficulty = "Easy"
            ),
            Question(
                id = "pyq_04",
                subject = "Physics",
                questionText = "Punyansu AI Predicted 2026: What is the dimensional formula for Planck's Constant (h)?",
                options = listOf("[M L^2 T^-1]", "[M L T^-1]", "[M L^2 T^-2]", "[M^0 L^2 T^-1]"),
                correctOptionIndex = 0,
                explanation = "Energy E = h * v => h = E / v = [M L^2 T^-2] / [T^-1] = [M L^2 T^-1].",
                tag = "FUTURE 2026 AI PREDICTED",
                difficulty = "Medium"
            ),
            Question(
                id = "pyq_05",
                subject = "Odisha GK",
                questionText = "In which district of Odisha is the famous Hirakud Dam located?",
                options = listOf("Cuttack", "Sambalpur", "Ganjam", "Khurda"),
                correctOptionIndex = 1,
                explanation = "Hirakud Dam is built across the Mahanadi River in Sambalpur district, Odisha.",
                tag = "PAST 2025",
                difficulty = "Easy"
            ),
            Question(
                id = "pyq_06",
                subject = "Adult OSOU Competency",
                questionText = "Under OSOU Distance Learning policies, what is the maximum age limit for general UG degree programs?",
                options = listOf("25 Years", "35 Years", "45 Years", "No Upper Age Limit (Up to 55+ Yrs)"),
                correctOptionIndex = 3,
                explanation = "Odisha State Open University (OSOU) encourages lifelong adult education and permits candidates up to 55+ years old.",
                tag = "FUTURE 2026 AI PREDICTED",
                difficulty = "Easy"
            )
        )
    }

    fun getFlashcards(): List<Flashcard> {
        return listOf(
            Flashcard(
                id = "fc_01",
                subject = "Physics",
                questionOrTopic = "Ohm's Law Formula",
                answerOrConcept = "V = I × R (Voltage = Current × Resistance). Valid for ohmic conductors at constant temperature.",
                tag = "CORE FORMULA"
            ),
            Flashcard(
                id = "fc_02",
                subject = "Chemistry",
                questionOrTopic = "pH Value Scale Range",
                answerOrConcept = "0 to 14. Neutral is 7. <7 is Acidic, >7 is Basic/Alkaline. pH = -log10[H+].",
                tag = "QUICK RECALL"
            ),
            Flashcard(
                id = "fc_03",
                subject = "Mathematics",
                questionOrTopic = "Quadratic Formula",
                answerOrConcept = "x = [-b ± √(b² - 4ac)] / (2a). Discriminant D = b² - 4ac dictates root realness.",
                tag = "EXAM SHORTCUT"
            ),
            Flashcard(
                id = "fc_04",
                subject = "Odisha GK",
                questionOrTopic = "Capital & Classical Language of Odisha",
                answerOrConcept = "Bhubaneswar is the Capital. Odia was declared a Classical Language of India in 2014.",
                tag = "GENERAL KNOWLEDGE"
            ),
            Flashcard(
                id = "fc_05",
                subject = "Adult Edu / OSOU",
                questionOrTopic = "Lifelong Learning & Adult Edu Provisions",
                answerOrConcept = "OSOU distance programs accept candidates up to 55+ years old with flexible credit transfers & online study material.",
                tag = "ADULT LEARNING"
            ),
            Flashcard(
                id = "fc_06",
                subject = "Class 1-10 Science",
                questionOrTopic = "Photosynthesis Reaction",
                answerOrConcept = "6CO₂ + 6H₂O + Sunlight → C₆H₁₂O₆ + 6O₂ (Occurs in chloroplasts containing chlorophyll).",
                tag = "FOUNDATION SCIENCE"
            )
        )
    }

    fun getStudyInfoTopics(): List<StudyInfoTopic> {
        return listOf(
            StudyInfoTopic(
                id = "si_01",
                title = "AOEE & OJEE 2026-2027 Official Exam Schedule",
                category = "Exam Schedule",
                content = "• Online Application Release: January 2026\n• Admit Card Download: April 2026\n• CBT Examination Window: May 2026\n• Result & Merit Rank Cards: June 2026\n• Online Counseling Sessions: July 2026",
                targetGroup = "All Applicants"
            ),
            StudyInfoTopic(
                id = "si_02",
                title = "Eligibility & Age Criteria (Class 1 to 55 Years Old)",
                category = "Eligibility & Age Criteria",
                content = "• Class 1-10 Foundation: School students aged 6-16 (NRTS, NMMS, Navodaya)\n• OJEE Engineering / Medical: 10+2 CHSE/CBSE with Physics, Chemistry & Math/Bio (Ages 17-25)\n• CPET PG Entrance: Bachelor's Degree in relevant stream\n• OSOU Adult & Continuing Education: Open University courses accepting candidate adult learners UP TO 55 YEARS OLD for lifelong skill enhancement.",
                targetGroup = "All Age Groups (Ages 6 to 55)"
            ),
            StudyInfoTopic(
                id = "si_03",
                title = "Syllabus Breakdown & Subject Weightage",
                category = "Syllabus",
                content = "• OJEE B.Tech: 120 Questions (Physics 40, Chemistry 40, Math 40)\n• DET Diploma: 100 Questions (Science 40, Math 40, Aptitude 20)\n• CPET Degree: 70 Subject Specific + 30 General Reasoning\n• OSOU Adult Entrance: Basic Digital Literacy, Odia/English Comprehension, General Knowledge",
                targetGroup = "Stream Specific"
            ),
            StudyInfoTopic(
                id = "si_04",
                title = "Odisha Web-Counseling & Choice Filling",
                category = "Counseling",
                content = "1. Register at OJEE portal with AOEE Scorecard\n2. Pay counseling fee of ₹450 online\n3. Lock choices of government and private engineering/degree colleges in Odisha\n4. Seat allotment based on rank & category quota.",
                targetGroup = "Rank Holders"
            )
        )
    }

    private fun createSchoolFoundationMockTest(): MockTest {
        return MockTest(
            id = "school_foundation_1",
            title = "Class 1-10 Primary Talent & NRTS Foundation Mock",
            stream = ExamStream.SCHOOL_FOUNDATION,
            durationMinutes = 15,
            description = "Elementary & High School Aptitude for NRTS, NMMS, and Navodaya Olympiads.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "sf1",
                    subject = "Science",
                    questionText = "Which gas do plants absorb from the atmosphere during photosynthesis?",
                    options = listOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"),
                    correctOptionIndex = 1,
                    explanation = "Plants absorb Carbon Dioxide (CO2) from the air to perform photosynthesis.",
                    tag = "FOUNDATION 2025",
                    difficulty = "Easy"
                ),
                Question(
                    id = "sf2",
                    subject = "Mathematics",
                    questionText = "What is the smallest prime number?",
                    options = listOf("0", "1", "2", "3"),
                    correctOptionIndex = 2,
                    explanation = "2 is the smallest prime number and the only even prime number.",
                    tag = "FOUNDATION 2025",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createChseSecondaryMockTest(): MockTest {
        return MockTest(
            id = "chse_11_12_mock",
            title = "Class 11-12 CHSE / CBSE Board & Entrance Prep",
            stream = ExamStream.SECONDARY_CHSE,
            durationMinutes = 15,
            description = "High school science & mathematics questions for Class 11-12 students.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "ch1",
                    subject = "Physics",
                    questionText = "What is the acceleration due to gravity on the surface of the Earth?",
                    options = listOf("9.8 m/s²", "8.9 m/s²", "10.8 m/s²", "9.8 cm/s²"),
                    correctOptionIndex = 0,
                    explanation = "The standard acceleration due to gravity g on Earth's surface is 9.8 m/s².",
                    tag = "CHSE 2025",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createGraduationCpetMockTest(): MockTest {
        return MockTest(
            id = "cpet_mock_1",
            title = "Odisha CPET Degree & PG Entrance Mock",
            stream = ExamStream.GRADUATION_CPET,
            durationMinutes = 15,
            description = "For graduated students appearing for Common PG Entrance Test.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "c1",
                    subject = "General Reasoning",
                    questionText = "If CAT = 24 and DOG = 26, then PIG = ?",
                    options = listOf("32", "36", "38", "40"),
                    correctOptionIndex = 0,
                    explanation = "P(16) + I(9) + G(7) = 32.",
                    tag = "CPET 2025",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createAdultOsouMockTest(): MockTest {
        return MockTest(
            id = "adult_osou_mock",
            title = "OSOU Adult & Continuing Education Entrance (Up to 55 Yrs)",
            stream = ExamStream.ADULT_CONTINUING_ED,
            durationMinutes = 15,
            description = "Digital literacy, adult aptitude & general knowledge for lifelong learners up to 55 years old.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "a1",
                    subject = "Digital Literacy",
                    questionText = "What does CPU stand for in computer systems?",
                    options = listOf("Central Processing Unit", "Central Power Unit", "Computer Program Utility", "Core Processing Unit"),
                    correctOptionIndex = 0,
                    explanation = "CPU stands for Central Processing Unit, the primary component that executes instructions.",
                    tag = "ADULT OSOU 2025",
                    difficulty = "Easy"
                ),
                Question(
                    id = "a2",
                    subject = "Odisha Heritage & GK",
                    questionText = "Who wrote the famous Odia epic 'Sarabala Mahabharata'?",
                    options = listOf("Fakir Mohan Senapati", "Sarala Das", "Upendra Bhanja", "Radhanath Ray"),
                    correctOptionIndex = 1,
                    explanation = "Sarala Das, the 15th-century poet, is regarded as the Adi Kabi of Odia literature.",
                    tag = "ADULT OSOU 2025",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createEngineeringMockTest1(): MockTest {
        return MockTest(
            id = "eng_mock_2025",
            title = "AOEE OJEE B.Tech 2025 Past Paper Test",
            stream = ExamStream.ENGINEERING,
            durationMinutes = 15,
            description = "Real past entrance questions from 2021-2025 covering Physics, Chemistry, and Mathematics.",
            isAiGenerated = false,
            questions = listOf(
                Question(
                    id = "e1",
                    subject = "Physics",
                    questionText = "An object is placed at a distance of 20 cm in front of a concave mirror of focal length 10 cm. The image is formed at:",
                    options = listOf("10 cm in front", "20 cm in front", "20 cm behind", "40 cm in front"),
                    correctOptionIndex = 1,
                    explanation = "Mirror formula: 1/f = 1/v + 1/u. u = -20 cm, f = -10 cm => 1/-10 = 1/v - 1/20 => v = -20 cm (20 cm in front of mirror).",
                    tag = "PAST 2024",
                    difficulty = "Medium"
                ),
                Question(
                    id = "e2",
                    subject = "Chemistry",
                    questionText = "The oxidation state of Chromium in K2Cr2O7 is:",
                    options = listOf("+3", "+4", "+6", "+7"),
                    correctOptionIndex = 2,
                    explanation = "2(+1) + 2(x) + 7(-2) = 0 => 2 + 2x - 14 = 0 => 2x = 12 => x = +6.",
                    tag = "PAST 2023",
                    difficulty = "Easy"
                ),
                Question(
                    id = "e3",
                    subject = "Mathematics",
                    questionText = "If A is a square matrix of order 3 and |A| = 5, then |adj A| is equal to:",
                    options = listOf("5", "25", "125", "1/5"),
                    correctOptionIndex = 1,
                    explanation = "|adj A| = |A|^(n-1) = 5^(3-1) = 5^2 = 25.",
                    tag = "PAST 2025",
                    difficulty = "Medium"
                ),
                Question(
                    id = "e4",
                    subject = "Physics",
                    questionText = "Work done in an adiabatic process for an ideal gas depends on change in:",
                    options = listOf("Volume only", "Temperature", "Pressure only", "Density"),
                    correctOptionIndex = 1,
                    explanation = "For adiabatic process Q = 0, so W = -dU = n * Cv * (T1 - T2), depending directly on temperature change.",
                    tag = "PAST 2022",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createEngineeringFutureMockTest(): MockTest {
        return MockTest(
            id = "eng_future_2026",
            title = "Punyansu AI 2026-2027 Predicted Future Mock Test",
            stream = ExamStream.ENGINEERING,
            durationMinutes = 20,
            description = "AI-generated questions based on deep learning pattern analysis of 10 years of Odisha entrance exams.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "ef1",
                    subject = "Physics",
                    questionText = "[Punyansu AI 2026 Prediction] The de Broglie wavelength of an electron accelerated through a potential difference V volts is given by:",
                    options = listOf("12.27 / sqrt(V) Å", "1.227 / V Å", "122.7 / sqrt(V) Å", "0.1227 * sqrt(V) Å"),
                    correctOptionIndex = 0,
                    explanation = "λ = h / sqrt(2m eV) = 12.27 / sqrt(V) Angstroms. Extremely high probability for AOEE 2026!",
                    tag = "FUTURE 2026 AI PREDICTED",
                    difficulty = "Hard"
                ),
                Question(
                    id = "ef2",
                    subject = "Chemistry",
                    questionText = "[Punyansu AI 2026 Prediction] Which of the following complex ions is diamagnetic?",
                    options = listOf("[Fe(CN)6]^3-", "[Fe(H2O)6]^3+", "[Co(NH3)6]^3+", "[NiCl4]^2-"),
                    correctOptionIndex = 2,
                    explanation = "Co3+ is d6. Strong field ligand NH3 causes complete pairing resulting in t2g6 eg0 (zero unpaired electrons, diamagnetic).",
                    tag = "FUTURE 2026 AI PREDICTED",
                    difficulty = "Hard"
                ),
                Question(
                    id = "ef3",
                    subject = "Mathematics",
                    questionText = "[Punyansu AI 2027 Prediction] The solution of differential equation dy/dx + y = e^(-x) is:",
                    options = listOf("y e^x = x + C", "y e^(-x) = x + C", "y = x e^x + C", "y = e^(-x) + C"),
                    correctOptionIndex = 0,
                    explanation = "Integrating factor IF = e^(∫1 dx) = e^x. Thus y * e^x = ∫ e^(-x) * e^x dx = ∫ 1 dx = x + C.",
                    tag = "FUTURE 2027 HIGH PROBABILITY",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createDiplomaMockTest1(): MockTest {
        return MockTest(
            id = "diploma_mock_1",
            title = "Odisha DET Polytechnic Entrance Mock Test",
            stream = ExamStream.DIPLOMA_DET,
            durationMinutes = 15,
            description = "Covers Class 10 Science, Math, and General Aptitude for Odisha Diploma Admission.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "d1",
                    subject = "Science",
                    questionText = "What is the SI unit of electric resistance?",
                    options = listOf("Volt", "Ampere", "Ohm", "Watt"),
                    correctOptionIndex = 2,
                    explanation = "The SI unit of resistance is Ohm (Ω), named after Georg Simon Ohm.",
                    tag = "PAST 2024",
                    difficulty = "Easy"
                ),
                Question(
                    id = "d2",
                    subject = "Mathematics",
                    questionText = "The sum of roots of quadratic equation 2x^2 - 8x + 6 = 0 is:",
                    options = listOf("2", "3", "4", "8"),
                    correctOptionIndex = 2,
                    explanation = "Sum of roots = -b/a = -(-8)/2 = 8/2 = 4.",
                    tag = "FUTURE 2026 AI PREDICTED",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createDiplomaFutureMockTest(): MockTest {
        return MockTest(
            id = "diploma_future_2",
            title = "Punyansu AI DET 2026 Special Mock Test",
            stream = ExamStream.DIPLOMA_DET,
            durationMinutes = 15,
            description = "Specially generated DET future prediction questions.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "df1",
                    subject = "General Aptitude",
                    questionText = "Complete the series: 2, 6, 12, 20, 30, ?",
                    options = listOf("36", "40", "42", "50"),
                    correctOptionIndex = 2,
                    explanation = "Differences: +4, +6, +8, +10 => Next difference is +12 => 30 + 12 = 42.",
                    tag = "FUTURE 2026 AI PREDICTED",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createMedicalMockTest1(): MockTest {
        return MockTest(
            id = "med_mock_1",
            title = "OJEE Medical & Pharmacy 2025 Test",
            stream = ExamStream.MEDICAL_PHARMA,
            durationMinutes = 20,
            description = "Physics, Chemistry & Biology focused entrance test.",
            isAiGenerated = false,
            questions = listOf(
                Question(
                    id = "m1",
                    subject = "Biology",
                    questionText = "Which organelle is known as the powerhouse of the cell?",
                    options = listOf("Ribosome", "Mitochondria", "Golgi Apparatus", "Lysosome"),
                    correctOptionIndex = 1,
                    explanation = "Mitochondria produce cellular energy in the form of ATP.",
                    tag = "PAST 2023",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createBedCpetMockTest1(): MockTest {
        return MockTest(
            id = "bed_mock_1",
            title = "Odisha B.Ed / OTET Teaching Aptitude Test",
            stream = ExamStream.TEACHER_OTET,
            durationMinutes = 15,
            description = "General Awareness, Odisha Culture, Teaching Aptitude & Reasoning.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "b1",
                    subject = "Teaching Aptitude",
                    questionText = "What is the best way to handle a student who frequently asks questions in class?",
                    options = listOf("Scold them", "Encourage them to inquire further", "Ignore them", "Ask them to leave"),
                    correctOptionIndex = 1,
                    explanation = "Encouraging curiosity builds critical thinking and active learning.",
                    tag = "FUTURE 2026 AI PREDICTED",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createNavodayaMockTest(): MockTest {
        return MockTest(
            id = "navodaya_jnvst_1",
            title = "JNVST Navodaya Entrance Mock (Class 6 & 9)",
            stream = ExamStream.NAVODAYA_ENTRANCE,
            durationMinutes = 15,
            description = "Mental Ability, Arithmetic & Language Comprehension for Class 5th & 8th candidates.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "jnv1",
                    subject = "Mental Ability",
                    questionText = "Which figure is different from the other three?",
                    options = listOf("Square with 4 sides", "Triangle with 3 sides", "Circle with no straight edges", "Pentagon with 5 sides"),
                    correctOptionIndex = 2,
                    explanation = "A circle has a curved boundary while all others are polygon figures made of straight lines.",
                    tag = "JNVST 2026 PREDICTED",
                    difficulty = "Easy"
                ),
                Question(
                    id = "jnv2",
                    subject = "Arithmetic",
                    questionText = "Find the HCF of 24 and 36.",
                    options = listOf("6", "12", "18", "24"),
                    correctOptionIndex = 1,
                    explanation = "Factors of 24: 1,2,3,4,6,8,12,24. Factors of 36: 1,2,3,4,6,9,12,18,36. HCF is 12.",
                    tag = "JNVST 2025",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createPathaniSamantaMockTest(): MockTest {
        return MockTest(
            id = "pathani_samanta_1",
            title = "Pathani Samanta Mathematics Scholarship (PSMSE)",
            stream = ExamStream.PATHANI_SAMANTA,
            durationMinutes = 15,
            description = "Advanced Mathematical reasoning & problem solving for Odisha school students.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "psm1",
                    subject = "Mathematics",
                    questionText = "If x + 1/x = 4, what is the value of x² + 1/x²?",
                    options = listOf("12", "14", "16", "18"),
                    correctOptionIndex = 1,
                    explanation = "(x + 1/x)² = x² + 1/x² + 2 => 4² = x² + 1/x² + 2 => 16 - 2 = 14.",
                    tag = "PSMSE 2026",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createOavMockTest(): MockTest {
        return MockTest(
            id = "oav_entrance_1",
            title = "Odisha Adarsha Vidyalaya Entrance (OAV Class 6 & 9)",
            stream = ExamStream.OAV_ENTRANCE,
            durationMinutes = 15,
            description = "English, Odia, Math, Science & Social Studies for OAV Model School Selection.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "oav1",
                    subject = "Science",
                    questionText = "Which pigment gives green color to leaves?",
                    options = listOf("Hemoglobin", "Chlorophyll", "Xanthophyll", "Carotene"),
                    correctOptionIndex = 1,
                    explanation = "Chlorophyll is the green pigment in chloroplasts essential for photosynthesis.",
                    tag = "OAV 2025",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createCtEntranceMockTest(): MockTest {
        return MockTest(
            id = "ct_del_ed_1",
            title = "Odisha CT / D.El.Ed Teacher Entrance Mock",
            stream = ExamStream.CT_ENTRANCE,
            durationMinutes = 15,
            description = "Child Development, Pedagogy, Odia, English, Science & Social Studies.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "ct1",
                    subject = "Pedagogy & Child Dev",
                    questionText = "According to Jean Piaget, which stage of cognitive development occurs in children aged 2-7 years?",
                    options = listOf("Sensory-motor stage", "Pre-operational stage", "Concrete operational", "Formal operational"),
                    correctOptionIndex = 1,
                    explanation = "Pre-operational stage ranges from 2 to 7 years characterized by symbolic thinking.",
                    tag = "CT 2026 AI PREDICTED",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createOjeeAllMockTest(): MockTest {
        return MockTest(
            id = "ojee_all_1",
            title = "OJEE Complete Entrance Mock (B.Tech/Pharm/MCA/MBA)",
            stream = ExamStream.OJEE_ALL,
            durationMinutes = 15,
            description = "Combined mock test for all undergraduate and postgraduate professional courses under OJEE Odisha.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "ojall1",
                    subject = "Quantitative & General Ability",
                    questionText = "A train 150m long passes a telegraph pole in 10 seconds. What is the speed of the train in km/h?",
                    options = listOf("54 km/h", "60 km/h", "72 km/h", "45 km/h"),
                    correctOptionIndex = 0,
                    explanation = "Speed = Distance / Time = 150/10 = 15 m/s. 15 * (18/5) = 54 km/h.",
                    tag = "OJEE 2026",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createOasIasCivilMockTest(): MockTest {
        return MockTest(
            id = "oas_ias_civil_1",
            title = "OAS / IAS Civil Services Prelims (OPSC / UPSC)",
            stream = ExamStream.OAS_IAS_CIVIL,
            durationMinutes = 15,
            description = "General Studies, Odisha History, Polity, CSAT & Administrative Aptitude.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "oas1",
                    subject = "Odisha History & Heritage",
                    questionText = "The famous Kalinga War took place in which year BC?",
                    options = listOf("261 BC", "326 BC", "185 BC", "78 BC"),
                    correctOptionIndex = 0,
                    explanation = "Emperor Ashoka fought the Kalinga War in 261 BC on the banks of Daya River near Dhauli.",
                    tag = "OPSC OAS 2026",
                    difficulty = "Medium"
                )
            )
        )
    }

    private fun createOdishaAllGovtMockTest(): MockTest {
        return MockTest(
            id = "odisha_all_govt_1",
            title = "Odisha All Govt & Technical Entrance Combined Mock",
            stream = ExamStream.ODISHA_ALL_ENTRANCE,
            durationMinutes = 15,
            description = "Odisha GK, General English, Quantitative Reasoning for OSSSC, OSSC & State Examinations.",
            isAiGenerated = true,
            questions = listOf(
                Question(
                    id = "og1",
                    subject = "Odisha GK",
                    questionText = "Which river is known as the 'Sorrow of Odisha' prior to Hirakud Dam construction?",
                    options = listOf("Mahanadi", "Brahmani", "Baitarani", "Vamsadhara"),
                    correctOptionIndex = 0,
                    explanation = "Mahanadi was known as the Sorrow of Odisha due to frequent devastating floods before the construction of Hirakud Dam.",
                    tag = "ODISHA GOVT 2026",
                    difficulty = "Easy"
                )
            )
        )
    }

    private fun createItiPolytechnicMockTest1(): MockTest {
        return MockTest(
            id = "iti_mock_1",
            title = "Odisha ITI / Trade Aptitude Test",
            stream = ExamStream.ITI_POLYTECHNIC,
            durationMinutes = 10,
            description = "Basic Science, Numerical Aptitude & Trade Fundamentals.",
            isAiGenerated = false,
            questions = listOf(
                Question(
                    id = "i1",
                    subject = "Basic Trade Science",
                    questionText = "Which instrument is used to measure electric current?",
                    options = listOf("Voltmeter", "Ammeter", "Ohmmeter", "Galvanometer"),
                    correctOptionIndex = 1,
                    explanation = "An Ammeter connected in series measures electric current in amperes.",
                    tag = "PAST 2024",
                    difficulty = "Easy"
                )
            )
        )
    }
}

