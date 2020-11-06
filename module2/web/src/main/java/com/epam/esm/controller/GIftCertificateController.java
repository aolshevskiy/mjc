package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ExceptionDetail;
import com.epam.esm.exception.LocalizedControllerException;
import com.epam.esm.exception.UpdatingForbiddenFieldsException;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GIftCertificateController {

    GiftCertificateService service;
    ObjectMapper mapper;

    @PostMapping("/giftCertificates")
    public void save(@RequestBody GiftCertificateDto dto) throws LocalizedControllerException {
        try {
            service.save(dto);
        } catch (DuplicateKeyException ex) {
            throw new LocalizedControllerException(ExceptionDetail.NAME_IS_NOT_UNIQUE);
        } catch (DataIntegrityViolationException ex) {
            throw new LocalizedControllerException(ExceptionDetail.CERTIFICATE_TAGS_IS_NOT_AVAILABLE);
        }
    }

    @PatchMapping("/giftCertificates")
    public void update(@RequestBody GiftCertificateDto dto) throws LocalizedControllerException {
        try {
            service.update(dto);
        } catch (UpdatingForbiddenFieldsException ex) {
            throw new LocalizedControllerException(ExceptionDetail.UPDATING_FORBIDDEN_DATE_FIELDS);
        } catch (DuplicateKeyException ex) {
            throw new LocalizedControllerException(ExceptionDetail.NAME_IS_NOT_UNIQUE);
        }
    }

    @GetMapping("/giftCertificates")
    public String getAll() {
        try {
            return mapper.writeValueAsString(service.getAll());
        } catch (IOException ex) {
            //TODO use unregister exception instead
            throw new LocalizedControllerException(ExceptionDetail.INTERNAL_SERVER_ERROR);
        }

        //emptyresultsetexception
    }


    //TODO this method is refactoring now.
    // It will be able returns certificates by tag, part name or description and sort them.
    @GetMapping("/giftCertificates/{name}")
    public String get(@PathVariable String name) throws IOException {
        return mapper.writeValueAsString(service.get(name));
        //emptyresultsetexception
    }

    @DeleteMapping("/giftCertificates")
    public void delete(@RequestBody GiftCertificate certificate) {
        service.delete(certificate);
    }
}
