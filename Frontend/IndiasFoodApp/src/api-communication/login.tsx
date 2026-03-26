import { type NavigateFunction } from "react-router-dom";
import { login } from "../Api/ffb/accountApi";
import type { LoginRequest } from "../Api/generated/ffbAPI.schemas";
import type { LoginResult } from "../Api/ffb/types";


interface HandleLoginSubmitProps {
  ticketNumber: string;
  password: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
  setIsLoading: React.Dispatch<React.SetStateAction<boolean>>;
  navigate: NavigateFunction;
}

const handleLoginSubmit = async ({ ticketNumber, password, setError, setIsLoading, navigate,}: HandleLoginSubmitProps, e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const payload: LoginRequest = {
        loginNr: ticketNumber,
        password,
      };
      const data: LoginResult = await login(payload);
      console.log(data);
      localStorage.setItem('authToken', data.token);
      navigate('/home');
    } catch (loginError) {
      console.error('Login failed:', loginError);
      if (loginError instanceof TypeError) {
        setError('Backend nicht erreichbar (Netzwerk/CORS). Bitte Dev-Server neu starten und URL prüfen.');
      } else {
        setError('Login fehlgeschlagen. Bitte Zugangsdaten prüfen und erneut versuchen.');
      }
    } finally {
      setIsLoading(false);
    }
}
export default handleLoginSubmit;