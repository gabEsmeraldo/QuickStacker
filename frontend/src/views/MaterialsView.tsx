import { useState, type FormEvent } from "react";
import CrudTable from "../components/CrudTable";

interface Material {
  id: number;
  name: string;
  code: string;
  supplier: string;
  stock: number;
  minStock: number;
}

export default function MaterialsView() {
  const [materials, setMaterials] = useState<Material[]>([]);
  const [form, setForm] = useState({
    name: "",
    code: "",
    supplier: "",
    stock: "",
    minStock: "",
  });

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();

    const newItem: Material = {
      id: Date.now(),
      name: form.name,
      code: form.code,
      supplier: form.supplier,
      stock: Number(form.stock || 0),
      minStock: Number(form.minStock || 0),
    };

    setMaterials((prev) => [...prev, newItem]);

    setForm({
      name: "",
      code: "",
      supplier: "",
      stock: "",
      minStock: "",
    });
  };

  const handleDelete = (id: number) => {
    setMaterials((prev) => prev.filter((x) => x.id !== id));
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
                  value={form.name}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, name: e.target.value }))
                  }
                  placeholder="Ex.: Ácido cítrico"
                  required
                />
              </div>

              <div className="form-row">
                <label>Código interno</label>
                <input
                  value={form.code}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, code: e.target.value }))
                  }
                  placeholder="Ex.: MP-001"
                  required
                />
              </div>

              <div className="form-row">
                <label>Fornecedor</label>
                <input
                  value={form.supplier}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, supplier: e.target.value }))
                  }
                  placeholder="Ex.: Indústrias Brasil"
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

              <div className="form-row">
                <label>Estoque mínimo</label>
                <input
                  type="number"
                  min={0}
                  value={form.minStock}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, minStock: e.target.value }))
                  }
                  placeholder="0"
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">Cadastrar matéria-prima</button>
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

        <div className="table-wrapper">
          {materials.length === 0 ? (
            <div className="empty-state">
              Nenhum item cadastrado ainda.
            </div>
          ) : (
            <CrudTable
              data={materials.map((mp) => ({
                id: mp.id,
                Nome: mp.name,
                Código: mp.code,
                Fornecedor: mp.supplier,
                Estoque: mp.stock,
                "Min. Estoque": mp.minStock,
              }))}
              onDelete={handleDelete}
            />
          )}
        </div>
      </section>
    </>
  );
}
