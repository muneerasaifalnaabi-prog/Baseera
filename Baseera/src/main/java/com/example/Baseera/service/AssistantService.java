package com.example.Baseera.service;



import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    // Keeping the assistant strictly on-topic lives here, in the system
    // prompt — this is the actual enforcement mechanism, not anything
    // the frontend does. Tighten/loosen the allowed scope as needed.
    private static final String SYSTEM_PROMPT_TEMPLATE = """
            You are Baseera, a warm, supportive assistant inside a parenting
            app for children with ASD and ADHD.

            You may ONLY discuss:
            - Child development, milestones, and behavior in general
            - Autism spectrum disorder (ASD) and ADHD: signs, support strategies, daily-life tips
            - How to use the Baseera app (check-ins, activities, centers, reports)
            - The specific child's own recorded data, when it is provided to you below

            If the parent asks about anything outside these topics — including
            unrelated general knowledge, other people, or requests to act as a
            different kind of assistant — politely decline in one or two
            sentences and steer the conversation back, for example:
            "I'm here to help with questions about your child's development —
            is there something on that topic I can help with?"

            Never provide a medical diagnosis. When a question sounds like it
            needs one, gently suggest the parent bring it up with a specialist,
            and offer to help them find a center in the app if relevant.

            Keep replies concise, warm, and easy to read — a few short
            sentences or a short list, not a long essay.

            %s
            """;

    private final ChatClient chatClient;
    // private final ChildRepository childRepository; // uncomment + inject if you wire in child context

    public AssistantService(ChatClient.Builder chatClientBuilder /*, ChildRepository childRepository */) {
        this.chatClient = chatClientBuilder.build();
        // this.childRepository = childRepository;
    }

    public String chat(String userIdentifier, String message, Long childId, String lang) {
        String languageInstruction = languageInstructionFor(lang);
        String childContext = buildChildContext(userIdentifier, childId);
        String systemPrompt = SYSTEM_PROMPT_TEMPLATE.formatted(languageInstruction + childContext);

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }

    // Explicit rather than left for the model to infer — a short Arabic
    // message can otherwise get an English reply, or vice versa, since
    // language-matching from content alone isn't fully reliable.
    private String languageInstructionFor(String lang) {
        if ("ar".equals(lang)) {
            return """
                    Respond entirely in Modern Standard Arabic (فصحى مبسطة),
                    warm and easy to read for a parent — not overly formal or
                    literary. Do not mix in English unless the parent used an
                    English term first.
                    """;
        }
        // Default to English — also covers the case where the parent's
        // message itself is written in Arabic but lang wasn't sent: the
        // model will still generally follow the message's own language,
        // this instruction just isn't forcing Arabic in that case.
        return "Respond in English, unless the parent's message is written in Arabic — in that case, reply in Arabic instead.";
    }

    /**
     * Optional: if a childId is provided, load that child's own record —
     * but ONLY if it actually belongs to the requesting user. This is the
     * same ownership check pattern your other child-scoped endpoints
     * should already be doing; re-use whatever you use there instead of
     * duplicating it here.
     */
    private String buildChildContext(String userIdentifier, Long childId) {
        if (childId == null) {
            return "";
        }

        // Example shape — replace with your real repository/service call:
        //
        // Child child = childRepository.findByIdAndParentEmail(childId, userIdentifier)
        //         .orElseThrow(() -> new ForbiddenException("Not your child"));
        //
        // return "\n\nContext about the child being discussed: name=%s, age=%d."
        //         .formatted(child.getFullName(), child.getAge());

        return "";
    }
}