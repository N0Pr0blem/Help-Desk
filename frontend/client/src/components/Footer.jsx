import "./Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-section">
        <h4>Контакты</h4>
        <p>Телефон: +375 (29) 343-09-44</p>
        <p>Телефон: +375 (17) 378-52-11</p>
        <p>Email: rup@ivcstat.by</p>
      </div>

      <div className="footer-section">
        <h4>Ссылки</h4>
        <a href="/">Главная страница</a>
        <a href="/profile">Личный кабинет</a>
        <a href="/register">Зарегистрироваться</a>
      </div>

      <div className="footer-section">
        <h4>Адрес</h4>
        <p>г. Минск, пр. Партизанский, 12А, ком. 8А</p>
        <p>Беларусь, 220070</p>
      </div>
    </footer>
  );
}

export default Footer;
