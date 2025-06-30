import { useEffect, useState } from "react";
import "./Toast.css";

function Toast({ message, duration = 4000, onClose }) {
  const [visibleClass, setVisibleClass] = useState("");

  useEffect(() => {
    if (!message) return;

    setVisibleClass("show");

    const hideTimeout = setTimeout(() => {
      setVisibleClass("hide");
    }, duration);

    const removeTimeout = setTimeout(() => {
      onClose();
    }, duration + 500);

    return () => {
      clearTimeout(hideTimeout);
      clearTimeout(removeTimeout);
    };
  }, [message, duration, onClose]);

  if (!message) return null;

  return (
    <div className={`toast ${visibleClass}`}>
      <p>{message}</p>
      <button onClick={onClose}>×</button>
    </div>
  );
}

export default Toast;
