INSERT INTO quest_badminton.m_mail_templates (key, subject, content)
VALUES ('register', 'Kích hoạt tài khoản',
        '<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <title>Kích hoạt tài khoản</title>
</head>

<body style="
  margin: 0;
  padding: 0;
  background-color: #f5f7fa;
  font-family: Arial, Helvetica, sans-serif;
">

<table width="100%" cellpadding="0" cellspacing="0" style="padding: 24px;">
  <tr>
    <td align="center">

      <table width="600" cellpadding="0" cellspacing="0"
             style="
               background: #ffffff;
               border-radius: 8px;
               box-shadow: 0 4px 12px rgba(0,0,0,0.05);
               overflow: hidden;
             ">

        <tr>
          <td style="
            background: #111827;
            padding: 24px;
            text-align: center;
            color: #ffffff;
          ">
            <h1 style="margin: 0; font-size: 24px;">
              Quest Badminton Club
            </h1>
            <p style="margin: 8px 0 0; font-size: 14px; color: #d1d5db;">
              Account Activation
            </p>
          </td>
        </tr>

        <tr>
          <td style="padding: 32px; color: #111827;">
            <h2 style="margin-top: 0;">
              Xin chào [:name] 👋
            </h2>

            <p style="font-size: 15px; line-height: 1.6;">
              Cảm ơn bạn đã đăng ký tài khoản tại
              <strong>Quest Badminton Club</strong>.
            </p>

            <p style="font-size: 15px; line-height: 1.6;">
              Vui lòng nhấn nút bên dưới để hoàn tất việc
              <strong>kích hoạt tài khoản</strong> của bạn.
            </p>

            <table cellpadding="0" cellspacing="0" style="margin: 32px auto;">
              <tr>
                <td align="center" style="background: #2563eb; border-radius: 6px;">
                  <a href="[:url]" target="_blank"
                     style="
                       display: inline-block;
                       padding: 14px 32px;
                       color: #ffffff;
                       text-decoration: none;
                       font-weight: bold;
                       font-size: 16px;
                     ">
                    Kích hoạt tài khoản
                  </a>
                </td>
              </tr>
            </table>

            <p style="font-size: 14px; color: #6b7280;">
              Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này.
            </p>
          </td>
        </tr>

        <tr>
          <td style="
            background: #f3f4f6;
            padding: 16px;
            text-align: center;
            font-size: 12px;
            color: #6b7280;
          ">
            © Quest Badminton Club <br />
            Đây là email tự động, vui lòng không phản hồi.
          </td>
        </tr>

      </table>

    </td>
  </tr>
</table>

</body>
</html>
')