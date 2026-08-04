export const translations = {
  sidebar: {
    en: {
      dashboard: 'Dashboard',
      switchChild: 'Switch child',
      assessment: 'Check-in',
      reports: 'Reports',
      activities: 'Activities',
      centers: 'Centers',
      admin: 'Admin dashboard',
      logout: 'Log out',
      menu: 'Menu',
      adminSection: 'Admin',

      // added for the navbar redesign
      brandName: 'Baseera',
      langSwitch: 'Switch language',
      guest: 'Guest'
    },
    ar: {
      dashboard: 'لوحة التحكم',
      switchChild: 'تبديل الطفل',
      assessment: 'تسجيل حالة',
      reports: 'التقارير',
      activities: 'الأنشطة',
      centers: 'المراكز',
      admin: 'لوحة تحكم الأدمن',
      logout: 'تسجيل الخروج',
      menu: 'القائمة',
      adminSection: 'الإدارة',

      // added for the navbar redesign
      brandName: 'بصيرة',
      langSwitch: 'تغيير اللغة',
      guest: 'زائر'
    }
  },

  selectChild: {
    en: {
      selectTitle: 'Select your child',
      selectSubtitle: 'Choose who you\'re checking in on today.',
      addTitle: 'Add your first child',
      addSubtitle: 'Just a name and a birth date — we\'ll take it from there.',
      addAnother: '+ Add another child',
      fullName: 'Full name',
      dateOfBirth: 'Date of birth',
      gender: 'Gender',
      male: 'Male',
      female: 'Female',
      continue: 'Continue',
      saving: 'Saving...',
      cancel: 'Cancel',
      yearsOld: 'years old',
      loadError: 'Could not load your children.',
      saveError: 'Could not save this child. Please check the details.'
    },
    ar: {
      selectTitle: 'اختر طفلك',
      selectSubtitle: 'اختر من تتابع أمره اليوم.',
      addTitle: 'أضف طفلك الأول',
      addSubtitle: 'فقط اسم وتاريخ ميلاد، وسنكمل الباقي.',
      addAnother: '+ إضافة طفل آخر',
      fullName: 'الاسم الكامل',
      dateOfBirth: 'تاريخ الميلاد',
      gender: 'الجنس',
      male: 'ذكر',
      female: 'أنثى',
      continue: 'متابعة',
      saving: '...',
      cancel: 'إلغاء',
      yearsOld: 'سنة',
      loadError: 'تعذّر تحميل بيانات أطفالك.',
      saveError: 'تعذّر حفظ بيانات الطفل. يرجى التحقق من التفاصيل.'
    }
  },

  assessment: {
    en: {
      title: 'How is your child doing?',
      subtitle: 'Tell us what you\'ve noticed, in your own words. We\'re here to listen, not to judge.',
      placeholder: 'For example: "He doesn\'t respond when I call his name, and gets very upset with small changes in routine..."',
      send: 'Send',
      thinking: 'Thinking this through with you...',
      resultTitle: 'Here\'s what we found',
      guidanceNote: 'Guidance, not a diagnosis.',
      riskLow: 'Looking good so far',
      riskMedium: 'Worth keeping an eye on',
      riskHigh: 'Worth a closer look',
      newAssessment: 'Start a new check-in',
      historyTitle: 'Past check-ins',
      noHistory: 'No check-ins yet for this child.',
      minLengthHint: 'A few sentences helps us understand better.',
      submitError: 'Something went wrong. Please try again.',
      noChildSelected: 'Please select a child first.'
    },
    ar: {
      title: 'كيف حال طفلك؟',
      subtitle: 'أخبرنا بما لاحظته، بكلماتك الخاصة. نحن هنا لنستمع، لا لنحكم.',
      placeholder: 'مثال: "لا يستجيب عندما أنادي اسمه، ويضطرب كثيراً من أي تغيير بسيط في روتينه..."',
      send: 'إرسال',
      thinking: 'نفكر في هذا معك...',
      resultTitle: 'إليك ما توصلنا إليه',
      guidanceNote: 'إرشاد، وليس تشخيصاً.',
      riskLow: 'الأمور تبدو جيدة حتى الآن',
      riskMedium: 'يستحق المتابعة',
      riskHigh: 'يستحق نظرة أقرب',
      newAssessment: 'ابدأ تسجيلاً جديداً',
      historyTitle: 'التسجيلات السابقة',
      noHistory: 'لا توجد تسجيلات بعد لهذا الطفل.',
      minLengthHint: 'بضع جمل تساعدنا على الفهم بشكل أفضل.',
      submitError: 'حدث خطأ ما. يرجى المحاولة مرة أخرى.',
      noChildSelected: 'يرجى اختيار طفل أولاً.'
    }
  },

  welcome: {
    en: {
      badge: 'Early awareness, gentle guidance',
      headline: 'You noticed something.',
      headlineAccent: 'We\'re here to help.',
      intro: 'Every parent knows their child best. Baseera helps you turn what you\'ve noticed into clear, calm next steps, with early awareness for autism and ADHD, guided by AI and grounded in care.',
      ctaPrimary: 'Get started',
      ctaSecondary: 'I already have an account',
      step1Title: 'Share what you see',
      step1Text: 'Describe your child\'s behaviour in your own words. No forms, no jargon.',
      step2Title: 'Get gentle insight',
      step2Text: 'Our AI reads what you wrote and offers early guidance, never a diagnosis.',
      step3Title: 'Take the next step',
      step3Text: 'Find nearby specialist centers and activities matched to your child.',
      reassurance: 'Baseera is a guide, not a doctor. Everything here is designed to support the conversation you have with a specialist, never to replace it.'
    },
    ar: {
      badge: 'وعي مبكر، إرشاد لطيف',
      headline: 'لاحظت شيئاً.',
      headlineAccent: 'نحن هنا لمساعدتك.',
      intro: 'كل والد يعرف طفله أكثر من غيره. بصيرة تساعدك على تحويل ما لاحظته إلى خطوات واضحة وهادئة، بوعي مبكر بالتوحد وفرط الحركة، بدعم من الذكاء الاصطناعي وبرعاية حقيقية.',
      ctaPrimary: 'ابدأ الآن',
      ctaSecondary: 'لدي حساب بالفعل',
      step1Title: 'شارك ما تلاحظه',
      step1Text: 'صف سلوك طفلك بكلماتك الخاصة. بدون استمارات، بدون مصطلحات معقدة.',
      step2Title: 'احصل على رؤية لطيفة',
      step2Text: 'الذكاء الاصطناعي يقرأ ما كتبته ويقدم إرشاداً مبكراً، وليس تشخيصاً.',
      step3Title: 'اتخذ الخطوة التالية',
      step3Text: 'اعثر على مراكز متخصصة قريبة وأنشطة مناسبة لطفلك.',
      reassurance: 'بصيرة دليل، وليست طبيباً. كل ما هنا مصمم لدعم حوارك مع المختص، لا ليحل محله.'
    }
  },

  dashboard: {
    en: {
      greeting: 'Welcome back',
      subtitle: 'Here\'s where things stand today.',
      noChildTitle: 'Let\'s start with your child',
      noChildText: 'Add your child to begin, it takes less than a minute.',
      addChild: 'Add your child',
      statChildren: 'Children',
      statCheckins: 'Check-ins',
      statLatest: 'Latest',
      noAssessment: 'No check-in yet',
      riskLow: 'Looking good so far',
      riskMedium: 'Worth keeping an eye on',
      riskHigh: 'Worth a closer look',
      quickTitle: 'What would you like to do?',
      quickCheckin: 'New check-in',
      quickCheckinText: 'Tell us what you\'ve noticed today',
      quickActivities: 'Browse activities',
      quickActivitiesText: 'Matched to your child\'s age and needs',
      quickCenters: 'Find centers',
      quickCentersText: 'Specialist support near you',
      quickReports: 'Reports',
      quickReportsText: 'Upload and review documents',
      historyTitle: 'Recent check-ins',
      loadError: 'Could not load your dashboard.',

      // added for the home redesign — hero
      heroBadge: 'AI-Powered Family Support',
      heroHeadline: 'Every small step deserves understanding.',
      heroSub: 'Baseera helps families understand their child\'s development through AI-powered assessments, personalized activities, trusted healthcare guidance, and secure medical records.',
      ctaStartAssessment: 'Start Assessment',
      ctaExploreActivities: 'Explore Activities',
      heroCardAi: 'AI Guidance',
      heroCardAiDesc: 'Personalized developmental insights',
      heroCardProgress: 'Progress Tracking',
      heroCardProgressDesc: 'Celebrate every milestone',
      heroCardSecure: 'Secure Records',
      heroCardSecureDesc: 'Protected and organized',

      // added — welcome card
      welcomeCardText: 'Every child develops differently, and every journey is unique. Baseera is here to help you understand, support, and celebrate every milestone with confidence.',
      ctaContinueJourney: 'Continue Journey',

      // added — overview cards without an existing equivalent
      overviewChildren: 'Children',
      overviewChildrenDesc: 'Manage profiles',
      overviewHistory: 'Assessment History',
      overviewHistoryDesc: 'Past check-ins',

      // added — AI assistant section
      aiBadge: 'AI Assistant',
      aiHeading: 'Ask Baseera AI anytime.',
      aiText: 'Have questions about developmental milestones, therapeutic activities, or understanding your child\'s progress? Baseera AI is available to provide supportive guidance, explain reports in simple language, and help you understand your next steps.',
      aiDisclaimer: 'Baseera AI offers supportive, educational guidance — not a medical diagnosis.',
      ctaStartChat: 'Start Chat',
      ctaLearnMore: 'Learn More',
      chatBubbleParent: 'My child avoids eye contact during play.',
      chatBubbleAi: 'That\'s a helpful observation. Here\'s what it may mean, and when a specialist visit could help.'
    },
    ar: {
      greeting: 'مرحباً بعودتك',
      subtitle: 'إليك ما وصلت إليه الأمور اليوم.',
      noChildTitle: 'لنبدأ بطفلك',
      noChildText: 'أضف طفلك لتبدأ، الأمر يستغرق أقل من دقيقة.',
      addChild: 'أضف طفلك',
      statChildren: 'الأطفال',
      statCheckins: 'التسجيلات',
      statLatest: 'الأحدث',
      noAssessment: 'لا يوجد تسجيل بعد',
      riskLow: 'الأمور تبدو جيدة حتى الآن',
      riskMedium: 'يستحق المتابعة',
      riskHigh: 'يستحق نظرة أقرب',
      quickTitle: 'ماذا تود أن تفعل؟',
      quickCheckin: 'تسجيل جديد',
      quickCheckinText: 'أخبرنا بما لاحظته اليوم',
      quickActivities: 'تصفح الأنشطة',
      quickActivitiesText: 'مطابقة لعمر طفلك واحتياجاته',
      quickCenters: 'ابحث عن مراكز',
      quickCentersText: 'دعم متخصص بالقرب منك',
      quickReports: 'التقارير',
      quickReportsText: 'ارفع وراجع المستندات',
      historyTitle: 'التسجيلات الأخيرة',
      loadError: 'تعذّر تحميل لوحة التحكم.',

      // added for the home redesign — hero
      heroBadge: 'دعم عائلي مدعوم بالذكاء الاصطناعي',
      heroHeadline: 'كل خطوة صغيرة تستحق أن تُفهم.',
      heroSub: 'تساعد بصيرة العائلات على فهم تطور أطفالهم من خلال تقييمات مدعومة بالذكاء الاصطناعي، وأنشطة مخصصة، وإرشاد صحي موثوق، وسجلات طبية آمنة.',
      ctaStartAssessment: 'ابدأ التقييم',
      ctaExploreActivities: 'استكشف الأنشطة',
      heroCardAi: 'إرشاد ذكي',
      heroCardAiDesc: 'رؤى تطويرية مخصصة',
      heroCardProgress: 'تتبع التقدم',
      heroCardProgressDesc: 'احتفل بكل إنجاز',
      heroCardSecure: 'سجلات آمنة',
      heroCardSecureDesc: 'محمية ومنظمة',

      // added — welcome card
      welcomeCardText: 'كل طفل يتطور بطريقته الخاصة، وكل رحلة فريدة من نوعها. بصيرة هنا لمساعدتك على الفهم والدعم، والاحتفال بكل إنجاز بثقة.',
      ctaContinueJourney: 'تابع الرحلة',

      // added — overview cards without an existing equivalent
      overviewChildren: 'الأطفال',
      overviewChildrenDesc: 'إدارة الملفات',
      overviewHistory: 'سجل التقييمات',
      overviewHistoryDesc: 'المتابعات السابقة',

      // added — AI assistant section
      aiBadge: 'المساعد الذكي',
      aiHeading: 'اسأل بصيرة الذكي في أي وقت.',
      aiText: 'هل لديك أسئلة حول مراحل النمو، أو الأنشطة العلاجية، أو فهم تقدم طفلك؟ بصيرة الذكي متاح لتقديم إرشاد داعم، وشرح التقارير بلغة بسيطة، ومساعدتك على فهم خطواتك التالية.',
      aiDisclaimer: 'يقدّم بصيرة الذكي إرشادًا داعمًا وتثقيفيًا — وليس تشخيصًا طبيًا.',
      ctaStartChat: 'ابدأ المحادثة',
      ctaLearnMore: 'اعرف المزيد',
      chatBubbleParent: 'طفلي يتجنب التواصل البصري أثناء اللعب.',
      chatBubbleAi: 'ملاحظة مفيدة. إليك ما قد تعنيه، ومتى قد تفيد زيارة أخصائي.'
    }
  }
};