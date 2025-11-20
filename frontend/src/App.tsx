import Layout from "./components/Layout";

import DashboardView from "./views/DashboardView";
import ProductsView from "./views/ProductsView";
import MaterialsView from "./views/MaterialsView"; // <-- ajuste conforme seu arquivo
import InputsView from "./views/InputsView";       // idem
import FormulasView from "./views/FormulasView";

import { useAuth } from "./context/AuthContext";
import LoginPage from "./views/LoginPage";

export default function App() {
  const { user } = useAuth();

  if (!user) return <LoginPage />;

  return (
    <Layout>
      {(view: string) => {
        switch (view) {
          case "dashboard":
            return <DashboardView />;
          case "products":
            return <ProductsView />;
          case "materials":
            return <MaterialsView />;
          case "inputs":
            return <InputsView />;
          case "formulas":
            return <FormulasView />;
          default:
            return <DashboardView />;
        }
      }}
    </Layout>
  );
}
