import React, { useEffect, useState } from "react";
import styles from "./LogInContainer.module.css";
import InputElement from "../Components/InputElement";
import { Button } from "@progress/kendo-react-buttons";
import { useNavigate } from "react-router-dom";
import { users, tickets, saveUsers, saveTickets } from "../Data";

function RegisterContainer() {
  const [loginNr, setLoginNr] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    setLoginNr("");
    setPassword("");
    setConfirmPassword("");
    setError("");
  }, []);

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    const trimmedLoginNr = loginNr.trim();

    // 🔴 Passwort check
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    // 🔴 User existiert bereits
    const existingUser = users.find((u) => u.login_Nr === trimmedLoginNr);

    if (existingUser) {
      setError("User already exists.");
      return;
    }

    // 🔴 Ticket muss existieren
    const validTicket = tickets.find((t) => t.login_Nr === trimmedLoginNr);

    if (!validTicket) {
      setError("Invalid ticket number.");
      return;
    }

    // ✅ User erstellen
    users.push({
      login_Nr: trimmedLoginNr,
      password: password,
      type: "GUEST",
    });

    // 💾 Users speichern
    saveUsers();

    // ✅ Ticket entfernen (einmal Nutzung)
    const ticketIndex = tickets.findIndex((t) => t.login_Nr === trimmedLoginNr);
    tickets.splice(ticketIndex, 1);

    // 💾 Tickets speichern
    saveTickets();

    // 👉 zurück zum Login
    navigate("/");
  };

  return (
    <div className={styles.Container}>
      <div className={styles.Title}>Create Account</div>

      {error && (
        <div
          style={{
            color: "red",
            marginBottom: "12px",
          }}
        >
          {error}
        </div>
      )}

      <form onSubmit={handleRegister}>
        <InputElement
          label="Login-Nr."
          editorId="login_nr"
          value={loginNr}
          onChange={setLoginNr}
          wrapperStyle={{ marginBottom: "10px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
        />

        <InputElement
          label="Password"
          editorId="password"
          value={password}
          onChange={setPassword}
          wrapperStyle={{ marginBottom: "10px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
          type="password"
        />

        <InputElement
          label="Confirm password"
          editorId="confirm_password"
          value={confirmPassword}
          onChange={setConfirmPassword}
          wrapperStyle={{ marginBottom: "16px" }}
          labelStyle={{ width: "100%" }}
          inputStyle={{
            height: "36px",
            border: error ? "1px solid red" : undefined,
          }}
          type="password"
        />

        <Button
          type="submit"
          style={{
            margin: "12px 0px",
            backgroundColor: "#2c2c2c",
            color: "white",
            height: "36px",
            fontSize: "large",
            width: "100%",
          }}
        >
          Create Account
        </Button>
      </form>

      <Button
        style={{
          margin: "6px 0px 16px 0px",
          backgroundColor: "#2c2c2c",
          color: "white",
          height: "36px",
          fontSize: "large",
        }}
        onClick={() => navigate("/")}
      >
        Login
      </Button>
    </div>
  );
}

export default RegisterContainer;
