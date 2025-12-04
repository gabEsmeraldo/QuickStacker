// src/views/CategoriasView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Categoria {
  idCategoria: number;
  descricao: string;
}

export default function CategoriasView() {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [form, setForm] = useState({ descricao: "" });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data: Categoria[] = await apiGet("/api/categorias");
        setCategorias(data);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar categorias";
        setError(errorMsg);
        console.error("Erro ao carregar categorias", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    const payload = { descricao: form.descricao };

    try {
      if (editingId) {
        const updated: Categoria = await apiPut(
          `/api/categorias/${editingId}`,
          payload
        );
        setCategorias((prev) =>
          prev.map((c) => (c.idCategoria === editingId ? updated : c))
        );
        setEditingId(null);
      } else {
        const created: Categoria = await apiPost("/api/categorias", payload);
        setCategorias((prev) => [...prev, created]);
      }

      setForm({ descricao: "" });
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar categoria";
      setError(errorMsg);
      console.error("Erro ao salvar categoria", err);
    }
  };

  const handleEdit = (cat: any) => {
    const id = cat.id ?? cat.idCategoria;
    setEditingId(id);
    setForm({ descricao: cat.Descrição ?? cat.descricao ?? "" });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir esta categoria?")) return;
    
    setError(null);
    try {
      await apiDelete(`/api/categorias/${id}`);
      setCategorias((prev) => prev.filter((c) => c.idCategoria !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir categoria";
      setError(errorMsg);
      console.error("Erro ao excluir categoria", err);
    }
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Categorias</h1>
          <p>Estruture a classificação dos produtos da indústria.</p>
        </div>
      </header>

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

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>{editingId ? "Editar categoria" : "Nova categoria"}</h2>
            <span className="card-subtitle">
              {editingId
                ? "Atualize os dados da categoria selecionada."
                : "Cadastre uma nova categoria de produto."}
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Descrição</label>
                <input
                  value={form.descricao}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, descricao: e.target.value }))
                  }
                  required
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar categoria"}
              </button>
            </div>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h3>Resumo</h3>
          </div>
          <div className="stats-row" style={{ gridTemplateColumns: "1fr" }}>
            <div className="stat-card">
              <div className="stat-label">Total de categorias</div>
              <div className="stat-value">{categorias.length}</div>
            </div>
          </div>
        </section>
      </div>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Lista de categorias</h2>
        </div>
        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : categorias.length === 0 ? (
            <div className="empty-state">Nenhuma categoria cadastrada.</div>
          ) : (
            <CrudTable
              data={categorias.map((c) => ({
                id: c.idCategoria,
                idCategoria: c.idCategoria,
                Descrição: c.descricao,
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
