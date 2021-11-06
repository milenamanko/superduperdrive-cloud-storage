package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final EncryptionService encryptionService;

    private final UserService userService;

    private final CredentialsMapper credentialsMapper;

    public CredentialService(EncryptionService encryptionService, UserService userService, CredentialsMapper credentialsMapper) {
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.credentialsMapper = credentialsMapper;
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialsMapper.getCredentials(credentialId);
    }

    public List<Credential> getAllCredentialsByUserId(Integer userId) {
        return credentialsMapper.getAllCredentials(userId);
    }

    public int insertCredential(Credential credential, Authentication authentication) {

        SecureRandom random = new SecureRandom();

        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        return credentialsMapper.insertCredentials(new Credential(null, credential.getUrl(),
                credential.getUsername(), encodedKey, encryptedPassword, userService.getUser(authentication.getName()).getUserId()));
    }

    public int deleteCredentialById(Integer credentialId) {
        return credentialsMapper.deleteCredentials(credentialId);
    }

    public int updateCredential(Credential credential, Authentication authentication) {

        SecureRandom random = new SecureRandom();

        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        credential.setUserId(userService.getUser(authentication.getName()).getUserId());

        return credentialsMapper.updateCredential(credential);
    }
}
