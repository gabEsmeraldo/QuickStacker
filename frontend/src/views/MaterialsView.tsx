import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface MateriaPrima {
  idMateriaPrima: number;
  nome: string;
  densidade: number | null;
  pesoUnitario: number | null;
}

export default function MaterialsView() {
  const [materials, setMaterials] = useState<MateriaPrima[]>([]);
  const [form, setForm] = useState({
    nome: "",
    densidade: "",
    pesoUnitario: "",
  });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data: MateriaPrima[] = await apiGet("/api/materias-primas");
        setMaterials(data);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar matérias-primas";
        setError(errorMsg);
        console.error("Erro ao carregar matérias-primas", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    const payload = {
      nome: form.nome,
      densidade: form.densidade ? parseFloat(form.densidade) : null,
      pesoUnitario: form.pesoUnitario ? parseFloat(form.pesoUnitario) : null,
    };

    try {
      if (editingId) {
        const updated: MateriaPrima = await apiPut(
          `/api/materias-primas/${editingId}`,
          payload
        );
        setMaterials((prev) =>
          prev.map((m) => (m.idMateriaPrima === editingId ? updated : m))
        );
        setEditingId(null);
      } else {
        const created: MateriaPrima = await apiPost("/api/materias-primas", payload);
        setMaterials((prev) => [...prev, created]);
      }

      setForm({ nome: "", densidade: "", pesoUnitario: "" });
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar matéria-prima";
      setError(errorMsg);
      console.error("Erro ao salvar matéria-prima", err);
    }
  };

  const handleEdit = (row: any) => {
    const id = row.id ?? row.idMateriaPrima;
    setEditingId(id);
    setForm({
      nome: row.Nome ?? row.nome ?? "",
      densidade: row.densidade ? String(row.densidade) : "",
      pesoUnitario: row.pesoUnitario ? String(row.pesoUnitario) : "",
    });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir esta matéria-prima?")) return;
    
    setError(null);
    try {
      await apiDelete(`/api/materias-primas/${id}`);
      setMaterials((prev) => prev.filter((m) => m.idMateriaPrima !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir matéria-prima";
      setError(errorMsg);
      console.error("Erro ao excluir matéria-prima", err);
    }
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Matérias-primas</h1>
          <p>Gerencie insumos essenciais para sua operação industrial.</p>
        </div>
      </header>

      {/* GRID: FORMULÁRIO + RESUMO */}
      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>Nova matéria-prima</h2>
            <span className="card-subtitle">
              Cadastre um novo insumo bruto utilizado na produção.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Nome da matéria-prima</label>
                <input
                  value={form.nome}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, nome: e.target.value }))
                  }
                  placeholder="Ex.: Ácido cítrico"
                  required
                />
              </div>

              <div className="form-row">
                <label>Densidade</label>
                <input
                  type="number"
                  step="0.01"
                  min={0}
                  value={form.densidade}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, densidade: e.target.value }))
                  }
                  placeholder="Ex.: 0.98"
                />
              </div>

              <div className="form-row">
                <label>Peso Unitário</label>
                <input
                  type="number"
                  step="0.01"
                  min={0}
                  value={form.pesoUnitario}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, pesoUnitario: e.target.value }))
                  }
                  placeholder="Ex.: 50.0"
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar matéria-prima"}
              </button>
            </div>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h3>Resumo rápido</h3>
          </div>

          <div className="stats-row" style={{ gridTemplateColumns: "1fr" }}>
            <div className="stat-card">
              <div className="stat-label">Total de matérias-primas</div>
              <div className="stat-value">{materials.length}</div>
            </div>
          </div>

          <p className="card-subtitle">
            Estoque mínimo é utilizado para alertas automáticos.
          </p>
        </section>
      </div>

      {/* LISTA */}
      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Lista de matérias-primas</h2>
        </div>

        {error && (
          <div className="error-message" style={{ 
            padding: "1rem", 
            margin: "1rem 0", 
            background: "#fee", 
            border: "1px solid #fcc", 
            borderRadius: "4px",
            color: "#c33"
          }}>
            {error}
          </div>
        )}

        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : materials.length === 0 ? (
            <div className="empty-state">
              Nenhum item cadastrado ainda.
            </div>
          ) : (
            <CrudTable
              data={materials.map((mp) => ({
                id: mp.idMateriaPrima,
                idMateriaPrima: mp.idMateriaPrima,
                Nome: mp.nome,
                Densidade: mp.densidade ?? "-",
                "Peso Unitário": mp.pesoUnitario ?? "-",
              }))}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          )}
        </div>
      </section>
    </>
  );
}
