// src/context/AuthContext.tsx

import {
  createContext,
  useState,
  useContext,
  useEffect,
  type ReactNode,
} from "react";

interface User {
  email: string;
  name?: string;
}

interface AuthContextProps {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  testModeEnabled: boolean;
  setTestModeEnabled: (enabled: boolean) => void;
}

const AuthContext = createContext<AuthContextProps | null>(null);

// Toggle for test mode - set to true to skip login
const TEST_MODE_ENABLED = true;
const TEST_EMAIL = "esmeraldo@gmail.com";
const TEST_PASSWORD = "12345678";

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [testModeEnabled, setTestModeEnabled] =
    useState<boolean>(TEST_MODE_ENABLED);

  const login = async (email: string, _password: string) => {
    // TODO: integrar API
    setUser({ email });
  };

  const register = async (name: string, email: string, _password: string) => {
    // TODO: integrar API
    setUser({ email, name });
  };

  const logout = () => {
    setUser(null);
    // Auto-login again if test mode is enabled
    if (testModeEnabled) {
      login(TEST_EMAIL, TEST_PASSWORD);
    }
  };

  // Auto-login when test mode is enabled
  useEffect(() => {
    if (testModeEnabled && !user) {
      setUser({ email: TEST_EMAIL });
    }
  }, [testModeEnabled, user]);

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        register,
        logout,
        testModeEnabled,
        setTestModeEnabled,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth deve estar dentro de AuthProvider");
  return ctx;
};
