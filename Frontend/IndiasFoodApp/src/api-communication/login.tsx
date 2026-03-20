import { useNavigate, type NavigateFunction } from "react-router-dom";
import type { paths } from "../types/api.generated";

type LoginRequestBody = paths['/account/login']['post']['requestBody']['content']['application/json'];
type LoginSuccessResponse = paths['/account/login']['post']['responses'][200]['content']['application/json'];

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
      const apiBaseUrl = import.meta.env.DEV
        ? '/api'
        : import.meta.env.VITE_API_BASE_URL || 'http://10.45.128.255:8080';
      const payload: LoginRequestBody = {
        loginNr: ticketNumber,
        password,
      };
      const response = await fetch(`${apiBaseUrl}/account/login`, {
        method: 'POST',
        credentials: 'include',
        headers: {
         'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Login failed (${response.status})`);
      }

      const data: LoginSuccessResponse = await response.json();
      console.log('Login successful:', data);
      if (data?.token) {
        localStorage.setItem('authToken', data.token);
      }

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