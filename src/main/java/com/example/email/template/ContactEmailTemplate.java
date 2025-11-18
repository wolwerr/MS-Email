package com.example.email.template;

import com.example.email.models.EmailModel;

import java.util.Optional;

public class ContactEmailTemplate {

    public String buildHtml(EmailModel emailModel) {

        // TELEFONE
        String rawPhone = Optional.ofNullable(emailModel.getPhone()).orElse("").trim();
        String phoneDigits = rawPhone.replaceAll("\\D", "");

        if (!phoneDigits.isBlank() && !phoneDigits.startsWith("55")) {
            phoneDigits = "55" + phoneDigits;
        }

        String whatsappButtonHtml = buildWhatsappButton(phoneDigits);

        // SAFETY
        String safeText = Optional.ofNullable(emailModel.getText()).orElse("");
        String safePhone = rawPhone.isBlank() ? "Não informado" : rawPhone;
        String safeEmailTo = Optional.ofNullable(emailModel.getEmailTo()).orElse("Não informado");

        // HTML FINAL
        return """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Contato recebido</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>

<body style="margin:0; padding:0; background-color:#f3f4f6; font-family:Arial, sans-serif;">

  <table width="100%%" cellpadding="0" cellspacing="0" style="padding:24px 0; background-color:#f3f4f6;">
    <tr>
      <td align="center">

        <table width="90%%" cellpadding="0" cellspacing="0"
               style="width:90%%; max-width:600px; background-color:#ffffff; border-radius:16px;
                      overflow:hidden; box-shadow:0 12px 30px rgba(15,23,42,0.12);">

          <!-- CABEÇALHO -->
          <tr>
            <td align="left"
                style="background:linear-gradient(135deg,#0f172a,#1d4ed8); padding:24px;">
              <h1 style="margin:0; color:#ffffff; font-size:20px; font-weight:600;">
                Novo contato recebido
              </h1>
              <p style="margin:4px 0 0; color:#cbd5f5; font-size:13px;">
                Estes são os detalhes enviados pelo formulário do sistema.
              </p>
            </td>
          </tr>

          <!-- CONTEÚDO -->
          <tr>
            <td style="padding:24px;">

              <p style="font-size:16px; color:#111827; margin:0 0 18px; line-height:1.6;">
                %s
              </p>

              <div style="font-size:14px; color:#111827; margin-bottom:12px;">
                <strong style="color:#6b7280;">Telefone:</strong> %s
              </div>

              <div style="font-size:14px; color:#111827; margin-bottom:12px;">
                <strong style="color:#6b7280;">E-mail de contato:</strong> %s
              </div>

            </td>
          </tr>

          %s

          <!-- RODAPÉ -->
          <tr>
            <td style="background-color:#f9fafb; padding:16px; text-align:center; font-size:11px; color:#6b7280;">
              Este e-mail foi gerado automaticamente pelo sistema de notificações.
            </td>
          </tr>

        </table>

      </td>
    </tr>
  </table>

</body>
</html>
""".formatted(
                safeText,
                safePhone,
                safeEmailTo,
                whatsappButtonHtml
        );
    }

    // BOTÃO WHATSAPP
    private String buildWhatsappButton(String phoneDigits) {
        if (phoneDigits == null || phoneDigits.isBlank()) {
            return "";
        }

        String whatsappLink = "https://wa.me/" + phoneDigits;

        return """
          <tr>
            <td style="padding: 20px 25px 30px 25px; text-align:center;">
              <a href="%s" target="_blank"
                 style="display:inline-block; padding:12px 24px; border-radius:999px;
                        background:linear-gradient(90deg,#22c55e,#16a34a);
                        color:#0b1120; text-decoration:none; font-weight:bold;
                        font-size:14px;">
                💬 Falar no WhatsApp
              </a>
            </td>
          </tr>
        """.formatted(whatsappLink);
    }
}
