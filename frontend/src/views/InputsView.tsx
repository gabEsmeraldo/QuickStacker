import { useState, type FormEvent } from "react";
import CrudTable from "../components/CrudTable";

interface InputItem {
  id: number;
  name: string;
  type: string;
  stock: number;
}

export default function InputsView() {
  const [inputs, setInputs] = useState<InputItem[]>([]);
  const [form, setForm] = useState({
    name: "",
    type: "",
    stock: "",
  });

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();

    const item: InputItem = {
      id: Date.now(),
      name: form.name,
      type: form.type,
      stock: Number(form.stock || 0),
    };

    setInputs((prev) => [...prev, item]);

    setForm({
      name: "",
      type: "",
      stock: "",
    });
  };

  const handleDelete = (id: number) => {
    setInputs((prev) => prev.filter((x) => x.id !== id));
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Insumos</h1>
          <p>Itens auxiliares utilizados nos processos produtivos.</p>
        </div>
      </header>

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>Novo insumo</h2>
            <span className="card-subtitle">
              Cadastre itens complementares da linha produtiva.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">

              <div className="form-row">
                <label>Nome do insumo</label>
                <input
                  value={form.name}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, name: e.target.value }))
                  }
                  placeholder="Ex.: Detergente industrial"
                  required
                />
              </div>

              <div className="form-row">
                <label>Categoria / tipo</label>
                <input
                  value={form.type}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, type: e.target.value }))
                  }
                  placeholder="Limpeza, embalagem, etc"
                />
              </div>

              <div className="form-row">
                <label>Estoque atual</label>
                <input
                  type="number"
                  min={0}
                  value={form.stock}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, stock: e.target.value }))
                  }
                  placeholder="0"
                />
              </div>

            </div>

            <div className="form-actions">
              <button className="btn btn-primary">Cadastrar insumo</button>
            </div>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h3>Resumo rápido</h3>
          </div>

          <div className="stats-row" style={{ gridTemplateColumns: "1fr" }}>
            <div className="stat-card">
              <div className="stat-label">Total de insumos</div>
              <div className="stat-value">{inputs.length}</div>
            </div>
          </div>

        </section>
      </div>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Lista de insumos</h2>
        </div>

        <div className="table-wrapper">
          {inputs.length === 0 ? (
            <div className="empty-state">Nenhum item cadastrado.</div>
          ) : (
            <CrudTable
              data={inputs.map((i) => ({
                id: i.id,
                Nome: i.name,
                Tipo: i.type,
                Estoque: i.stock,
              }))}
              onDelete={handleDelete}
            />
          )}
        </div>
      </section>
    </>
  );
}
