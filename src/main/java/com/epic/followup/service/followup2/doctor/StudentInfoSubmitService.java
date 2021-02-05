package com.epic.followup.service.followup2.doctor;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : zx
 * @version V1.0
 */
public interface StudentInfoSubmitService {

    boolean save(HttpSession session, MultipartFile file) throws IOException;
}
