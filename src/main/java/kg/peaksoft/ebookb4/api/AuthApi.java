package kg.peaksoft.ebookb4.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.ebookb4.dto.LoginRequest;
import kg.peaksoft.ebookb4.dto.ClientRegisterDTO;
import kg.peaksoft.ebookb4.dto.VendorRegisterDTO;
import kg.peaksoft.ebookb4.dto.response.JwtResponse;
import kg.peaksoft.ebookb4.config.jwt.JwtUtils;
import kg.peaksoft.ebookb4.config.services.UserDetailsImpl;
import kg.peaksoft.ebookb4.db.service.ClientService;
import kg.peaksoft.ebookb4.db.service.VendorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth API", description = "Registration and authentication")
public class AuthApi {

    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    VendorService vendorService;
    ClientService clientService;

    @Operation(summary = "Sign in", description = "Signing in for all users: admin, client, vendor")
    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, roles, userDetails.getFirstName()));
    }

    @Operation(summary = "Sign up", description = "Register only for client")
    @PostMapping("signup/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientRegisterDTO client) {
        return clientService.register(client, 1L);
    }

    @Operation(summary = "Sign up", description = "Register only for vendor")
    @PostMapping("signup/vendor")
    public ResponseEntity<?> registerVendor(@Valid @RequestBody VendorRegisterDTO vendor) {
        return vendorService.register(vendor, 2L);
    }

}
