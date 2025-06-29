import "./Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-section">
        <h4>Контакты</h4>
        <p>Телефон: +7 (495) 123-45-67</p>
        <p>Телефон: +7 (800) 555-35-35</p>
        <p>Email: support@helpdesk.com</p>
      </div>

      <div className="footer-section">
        <h4>Ссылки</h4>
        <a href="/profile">Личный кабинет</a>
        <a href="/create-task">Создать заявку</a>
        <a href="/admin">Панель админа</a>
      </div>

      <div className="footer-section">
        <h4>Адрес</h4>
        <p>г. Москва, ул. Примерная, д. 1</p>
        <p>Россия, 123456</p>
      </div>
    </footer>
  );
}

export default Footer;
