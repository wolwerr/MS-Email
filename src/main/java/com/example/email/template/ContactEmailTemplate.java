package com.example.email.template;

import com.example.email.models.EmailModel;

import java.util.Optional;

public class ContactEmailTemplate {

    public String buildHtml(EmailModel emailModel) {
        String rawPhone = Optional.ofNullable(emailModel.getPhone()).orElse("").trim();
        String phoneDigits = rawPhone.replaceAll("\\D", "");

        if (!phoneDigits.isBlank() && !phoneDigits.startsWith("55")) {
            phoneDigits = "55" + phoneDigits;
        }

        String whatsappButtonHtml = buildWhatsappButton(phoneDigits);

        String safeText = Optional.ofNullable(emailModel.getText()).orElse("");
        String safePhone = rawPhone.isBlank() ? "Não informado" : rawPhone;
        String safeEmailTo = Optional.ofNullable(emailModel.getEmailTo()).orElse("Não informado");

        return """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Contato recebido</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <style>
    @media (prefers-color-scheme: dark) {
      body { background-color:#020617 !important; color:#e5e7eb !important; }
      .card { background-color:#0b1120 !important; color:#e5e7eb !important; }
      .header { background-color:#020617 !important; }
      .footer { background-color:#020617 !important; color:#9ca3af !important; }
      .title { color:#e5e7eb !important; }
      .label { color:#9ca3af !important; }
      .value { color:#e5e7eb !important; }
    }
  </style>
</head>
<body style="margin:0; padding:0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif; background-color:#f3f4f6;">

  <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f3f4f6; padding: 24px 0;">
    <tr>
      <td align="center">

        <table width="600" cellpadding="0" cellspacing="0" class="card"
               style="background-color:#ffffff; border-radius:16px; overflow:hidden;
                      box-shadow:0 12px 30px rgba(15,23,42,0.12);">

          <tr>
            <td class="header"
                style="background:linear-gradient(135deg,#0f172a,#1d4ed8);
                       padding:24px 24px 20px 24px; text-align:left;">
              <h1 class="title"
                  style="margin:0; color:#ffffff; font-size:20px; font-weight:600;">
                Novo contato recebido
              </h1>
              <p style="margin:4px 0 0 0; color:#cbd5f5; font-size:13px;">
                Estes são os detalhes enviados pelo formulário do sistema.
              </p>
            </td>
          </tr>

          <tr>
            <td style="padding:24px 24px 8px 24px;">

              <p style="font-size:15px; color:#111827; margin:0 0 16px 0; line-height:1.5;">
                %s
              </p>

              <table cellpadding="0" cellspacing="0" width="100%%"
                     style="border-collapse:collapse; margin-top:8px;">

                <tr>
                  <td style="padding:8px 0; border-bottom:1px solid #e5e7eb; font-size:13px;">
                    <span class="label" style="font-weight:600; color:#6b7280;">Telefone:</span>
                    <span class="value" style="color:#111827; margin-left:4px;">%s</span>
                  </td>
                </tr>

                <tr>
                  <td style="padding:8px 0; border-bottom:1px solid #e5e7eb; font-size:13px;">
                    <span class="label" style="font-weight:600; color:#6b7280;">E-mail de contato:</span>
                    <span class="value" style="color:#111827; margin-left:4px;">%s</span>
                  </td>
                </tr>

              </table>

            </td>
          </tr>

          %s

          <tr>
            <td class="footer"
                style="background-color:#f9fafb; padding:14px 24px; text-align:center;
                       font-size:11px; color:#6b7280;">
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
