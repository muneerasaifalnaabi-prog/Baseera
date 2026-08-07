package com.example.Baseera.controller;



import com.example.Baseera.dto.request.ChatRequest;
import com.example.Baseera.dto.response.ChatResponse;
import com.example.Baseera.service.AssistantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    // Any authenticated parent or admin can use the assistant — adjust
    // if you want a narrower @PreAuthorize like the admin endpoints use.
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            Authentication authentication
    ) {
        // Swap this for however you currently resolve the authenticated
        // user elsewhere (e.g. a custom UserPrincipal, or looking the
        // user up by authentication.getName() if that's the email/id).
        String userIdentifier = authentication.getName();

        String reply = assistantService.chat(userIdentifier, request.message(), request.childId(), request.lang());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
