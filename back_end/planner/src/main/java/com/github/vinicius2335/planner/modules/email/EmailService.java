package com.github.vinicius2335.planner.modules.email;

import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Envia email para o participante
     * @param trip viagem
     * @throws EmailServiceException quando ocorrer um erro durante o envio de email
     */
    public void send(String participantEmail, Trip trip) throws EmailServiceException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        String content = createContent(trip);

        try {
            message.setFrom("noreply@example.org");
            message.setTo(participantEmail);
            message.setSubject("Confirmar viagem");
            message.setText(content, true);
            mailSender.send(message.getMimeMessage());

        } catch (MessagingException e) {
            throw new EmailServiceException("Erro ao tentar enviar o email para o participante: " + e.getMessage());
        }
    }

    private String createContent(Trip trip) {
        // ordem dos parametros: destination, formattedStartDate, formattedEndDate

        String pattern = "dd/MM/yyyy";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        String formattedStartDate = trip.getStartsAt().format(dtf);
        String formattedEndDate = trip.getEndsAt().format(dtf);

        return String.format(
                """
                <div style="font-family: sans-serif; font-size: 16px; line-height: 1.6;">
                          <p>Você foi convidado(a) para participar de uma viagem para <strong>%s</strong> nas datas de <strong>%s</strong> até <strong>%s</strong>.</p>
                          <p></p>
                          <p>Para confirmar sua presença na viagem, clique no link abaixo:</p>
                          <p></p>
                          <p>
                            <a href="#">Confirmar viagem</a>
                          </p>
                          <p></p>
                          <p>Caso você não saiba do que se trata esse e-mail, apenas ignore esse e-mail.</p>
                </div>
                """, trip.getDestination(), formattedStartDate, formattedEndDate

        );
    }
}
