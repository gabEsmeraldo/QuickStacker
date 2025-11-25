// src/views/ProductsView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Produto {
  idProduto: number;
  nome: string;
  validadeEmMeses: number | null;
  quantidadeTotal: number | null;
  categoria: {
    idCategoria: number;
    descricao: string;
  };
}

export default function ProductsView() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [categorias, setCategorias] = useState<Array<{ idCategoria: number; descricao: string }>>([]);
  const [form, setForm] = useState({ nome: "", validadeEmMeses: "", categoriaId: "" });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const [produtosData, categoriasData] = await Promise.all([
          apiGet("/api/produtos"),
          apiGet("/api/categorias")
        ]);
        setProdutos(produtosData);
        setCategorias(categoriasData);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar dados";
        setError(errorMsg);
        console.error("Erro ao carregar produtos", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!form.categoriaId) {
      setError("Selecione uma categoria");
      return;
    }

    const payload = {
      nome: form.nome,
      validadeEmMeses: form.validadeEmMeses ? parseInt(form.validadeEmMeses) : null,
      categoria: {
        idCategoria: parseInt(form.categoriaId)
      }
    };

    try {
      if (editingId) {
        const updated: Produto = await apiPut(
          `/api/produtos/${editingId}`,
          payload
        );
        setProdutos((prev) =>
          prev.map((p) => (p.idProduto === editingId ? updated : p))
        );
        setEditingId(null);
      } else {
        const created: Produto = await apiPost("/api/produtos", payload);
        setProdutos((prev) => [...prev, created]);
      }

      setForm({ nome: "", validadeEmMeses: "", categoriaId: "" });
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar produto";
      setError(errorMsg);
      console.error("Erro ao salvar produto", err);
    }
  };

  const handleEdit = (row: any) => {
    const id = row.id ?? row.idProduto;
    setEditingId(id);
    setForm({
      nome: row.Nome ?? row.nome ?? "",
      validadeEmMeses: row.validadeEmMeses ? String(row.validadeEmMeses) : "",
      categoriaId: row.categoria?.idCategoria ? String(row.categoria.idCategoria) : "",
    });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir este produto?")) return;
    
    setError(null);
    try {
      await apiDelete(`/api/produtos/${id}`);
      setProdutos((prev) => prev.filter((p) => p.idProduto !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir produto";
      setError(errorMsg);
      console.error("Erro ao excluir produto", err);
    }
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Produtos</h1>
          <p>Cadastre e gerencie os produtos finais da indústria.</p>
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
            <h2>{editingId ? "Editar produto" : "Novo produto"}</h2>
            <span className="card-subtitle">
              Informações básicas do produto.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Nome</label>
                <input
                  value={form.nome}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, nome: e.target.value }))
                  }
                  required
                />
              </div>
              <div className="form-row">
                <label>Validade (meses)</label>
                <input
                  type="number"
                  value={form.validadeEmMeses}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, validadeEmMeses: e.target.value }))
                  }
                  min="1"
                />
              </div>
              <div className="form-row">
                <label>Categoria</label>
                <select
                  value={form.categoriaId}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, categoriaId: e.target.value }))
                  }
                  required
                >
                  <option value="">Selecione uma categoria</option>
                  {categorias.map((cat) => (
                    <option key={cat.idCategoria} value={cat.idCategoria}>
                      {cat.descricao}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar produto"}
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
              <div className="stat-label">Total de produtos</div>
              <div className="stat-value">{produtos.length}</div>
            </div>
          </div>
        </section>
      </div>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Lista de produtos</h2>
        </div>

        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : produtos.length === 0 ? (
            <div className="empty-state">Nenhum produto cadastrado.</div>
          ) : (
            <CrudTable
              data={produtos.map((p) => ({
                id: p.idProduto,
                idProduto: p.idProduto,
                Nome: p.nome,
                "Validade (meses)": p.validadeEmMeses ?? "-",
                "Quantidade Total": p.quantidadeTotal ?? 0,
                Categoria: p.categoria?.descricao ?? "-",
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
