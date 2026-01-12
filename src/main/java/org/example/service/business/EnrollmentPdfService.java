package org.example.service.business;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.example.model.dto.pdf.EnrollmentFormPdfDto;
import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.mapper.EnrollmentFormPdfMapper;
import org.example.repository.EnrollmentFormRepository;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;


@Service
public class EnrollmentPdfService {

    private final EnrollmentFormRepository enrollmentFormRepository;
    private final TemplateEngine templateEngine;
    private final EnrollmentFormPdfMapper enrollmentFormPdfMapper;

    public EnrollmentPdfService(
            EnrollmentFormRepository enrollmentFormRepository,
            TemplateEngine templateEngine,
            EnrollmentFormPdfMapper enrollmentFormPdfMapper
    ) {
        this.enrollmentFormRepository = enrollmentFormRepository;
        this.templateEngine = templateEngine;
        this.enrollmentFormPdfMapper = enrollmentFormPdfMapper;
    }

    public byte[] generatePdf(Long formId, Long studentId) {
        EnrollmentFormEntity form = enrollmentFormRepository
                .findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment form not found"));

        if (form.getStudent().getId().longValue() != studentId.longValue()) {
            throw new SecurityException("Access denied");
        }

        EnrollmentFormPdfDto dto = enrollmentFormPdfMapper.toPdfDto(form);
        return renderPdf(dto);
    }

    /**
     * DTO -> Thymeleaf HTML -> PDF bytes
     */
    private byte[] renderPdf(EnrollmentFormPdfDto dto) {
        Context context = new Context();
        context.setVariable("dto", dto);

        String html = templateEngine.process("enrollmentFormPdf", context);
        html = html.replace("\uFEFF", "").trim();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Parse HTML -> W3C DOM (fixes a TON of XML/XHTML strictness issues)
            Document jsoupDoc = Jsoup.parse(html);
            jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withW3cDocument(w3cDoc, "http://localhost/");
            builder.toStream(out);
            builder.run();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
