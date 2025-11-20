// src/views/ProductsView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Produto {
  id: number;
  nome: string;
  descricao: string;
}

export default function ProductsView() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [form, setForm] = useState({ nome: "", descricao: "" });
  const [editingId, setEditingId] = useState<number | null>(null);

  useEffect(() => {
    async function load() {
      try {
        const data: Produto[] = await apiGet("/api/produtos");
        setProdutos(data);
      } catch (err) {
        console.error("Erro ao carregar produtos", err);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const payload = {
      nome: form.nome,
      descricao: form.descricao,
      // TODO: depois alinhar com Produto.java (categoria, etc.)
    };

    try {
      if (editingId) {
        const updated: Produto = await apiPut(
          `/api/produtos/${editingId}`,
          payload
        );
        setProdutos((prev) =>
          prev.map((p) => (p.id === editingId ? updated : p))
        );
        setEditingId(null);
      } else {
        const created: Produto = await apiPost("/api/produtos", payload);
        setProdutos((prev) => [...prev, created]);
      }

      setForm({ nome: "", descricao: "" });
    } catch (err) {
      console.error("Erro ao salvar produto", err);
    }
  };

  const handleEdit = (row: any) => {
    setEditingId(row.id);
    setForm({
      nome: row.Nome ?? row.nome,
      descricao: row.Descrição ?? row.descricao,
    });
  };

  const handleDelete = async (id: number) => {
    try {
      await apiDelete(`/api/produtos/${id}`);
      setProdutos((prev) => prev.filter((p) => p.id !== id));
    } catch (err) {
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

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>{editingId ? "Editar produto" : "Novo produto"}</h2>
            <span className="card-subtitle">
              Informações básicas do produto. Campos adicionais podem ser
              adicionados depois.
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
                <label>Descrição</label>
                <input
                  value={form.descricao}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, descricao: e.target.value }))
                  }
                />
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
          {produtos.length === 0 ? (
            <div className="empty-state">Nenhum produto cadastrado.</div>
          ) : (
            <CrudTable
              data={produtos.map((p) => ({
                id: p.id,
                Nome: p.nome,
                Descrição: p.descricao,
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
