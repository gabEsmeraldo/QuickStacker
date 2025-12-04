// src/views/FormulasView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Formula {
  idFormula: number;
  descricaoModoPreparo: string | null;
  produto: {
    idProduto: number;
    nome: string;
  };
}

interface MateriaPrima {
  idMateriaPrima: number;
  nome: string;
}

interface Insumo {
  idInsumo: number;
  nome: string;
}

interface MateriaPrimaRow {
  materiaPrimaId: string;
  quantidadeUtilizada: string;
}

interface InsumoRow {
  insumoId: string;
  quantidadeUtilizada: string;
}

interface FormulaHasMateriaPrima {
  formulaId: number;
  materiaPrimaId: number;
  quantidadeUtilizada: number;
  materiaPrima?: {
    idMateriaPrima: number;
    nome: string;
  };
}

interface FormulaHasInsumo {
  formula: {
    idFormula: number;
  };
  insumo: {
    idInsumo: number;
    nome: string;
  };
  quantidadeUtilizada: number;
}

export default function FormulasView() {
  const [formulas, setFormulas] = useState<Formula[]>([]);
  const [produtos, setProdutos] = useState<
    Array<{ idProduto: number; nome: string }>
  >([]);
  const [materiasPrimas, setMateriasPrimas] = useState<MateriaPrima[]>([]);
  const [insumos, setInsumos] = useState<Insumo[]>([]);
  const [form, setForm] = useState({
    produtoId: "",
    descricaoModoPreparo: "",
  });
  const [materiaPrimaRows, setMateriaPrimaRows] = useState<MateriaPrimaRow[]>(
    []
  );
  const [insumoRows, setInsumoRows] = useState<InsumoRow[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const [formulasData, produtosData, materiasPrimasData, insumosData] =
          await Promise.all([
            apiGet("/api/formulas"),
            apiGet("/api/produtos"),
            apiGet("/api/materias-primas"),
            apiGet("/api/insumos"),
          ]);
        setFormulas(formulasData);
        setProdutos(produtosData);
        setMateriasPrimas(materiasPrimasData);
        setInsumos(insumosData);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar fórmulas";
        setError(errorMsg);
        console.error("Erro ao carregar fórmulas", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!form.produtoId) {
      setError("Selecione um produto");
      return;
    }

    const produtoIdNum = parseInt(form.produtoId);
    if (isNaN(produtoIdNum)) {
      setError("ID do produto inválido");
      return;
    }

    const payload = {
      descricaoModoPreparo: form.descricaoModoPreparo || null,
      produtoId: produtoIdNum,
    };

    console.log("[FormulasView] Submitting payload:", JSON.stringify(payload));
    console.log("[FormulasView] produtoId type:", typeof payload.produtoId, "value:", payload.produtoId);

    try {
      let formulaId: number;

      if (editingId) {
        console.log("[FormulasView] Updating formula with id:", editingId);
        const updated: Formula = await apiPut(
          `/api/formulas/${editingId}`,
          payload
        );
        setFormulas((prev) =>
          prev.map((f) => (f.idFormula === editingId ? updated : f))
        );
        formulaId = editingId;
        setEditingId(null);
      } else {
        console.log("[FormulasView] Creating new formula");
        const created: Formula = await apiPost("/api/formulas", payload);
        console.log("[FormulasView] Formula created:", created);
        setFormulas((prev) => [...prev, created]);
        formulaId = created.idFormula;
      }

      // Delete existing relationships before adding new ones (for updates)
      if (editingId) {
        try {
          await apiDelete(`/api/formulas-materias-primas/formula/${formulaId}`);
          await apiDelete(`/api/formulas-insumos/formula/${formulaId}`);
        } catch (err) {
          // Ignore errors if relationships don't exist
          console.log("No existing relationships to delete or already deleted");
        }
      }

      // Add materias primas if provided
      if (materiaPrimaRows.length > 0) {
        const materiasPromises = materiaPrimaRows
          .filter((row) => row.materiaPrimaId && row.quantidadeUtilizada)
          .map((row) =>
            apiPost("/api/formulas-materias-primas", {
              formulaId: formulaId,
              materiaPrimaId: parseInt(row.materiaPrimaId),
              quantidadeUtilizada: parseFloat(row.quantidadeUtilizada),
            })
          );
        await Promise.all(materiasPromises);
      }

      // Add insumos if provided
      if (insumoRows.length > 0) {
        const insumosPromises = insumoRows
          .filter((row) => row.insumoId && row.quantidadeUtilizada)
          .map((row) => {
            const params = new URLSearchParams({
              formulaId: formulaId.toString(),
              insumoId: row.insumoId,
              quantidadeUtilizada: row.quantidadeUtilizada,
            });
            return apiPost(`/api/formulas-insumos?${params.toString()}`, {});
          });
        await Promise.all(insumosPromises);
      }

      setForm({ produtoId: "", descricaoModoPreparo: "" });
      setMateriaPrimaRows([]);
      setInsumoRows([]);
      setEditingId(null);
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar fórmula";
      setError(errorMsg);
      console.error("Erro ao salvar fórmula", err);
    }
  };

  const loadFormulaRelationships = async (formulaId: number) => {
    try {
      // Load materias primas and insumos for this formula
      const [materiasData, insumosData] = await Promise.all([
        apiGet(`/api/formulas-materias-primas/formula/${formulaId}`),
        apiGet(`/api/formulas-insumos/formula/${formulaId}`),
      ]);

      // Populate materiaPrimaRows
      const materiasRows: MateriaPrimaRow[] = materiasData.map(
        (mp: FormulaHasMateriaPrima) => ({
          materiaPrimaId: String(mp.materiaPrimaId),
          quantidadeUtilizada: String(mp.quantidadeUtilizada),
        })
      );
      setMateriaPrimaRows(materiasRows);

      // Populate insumoRows
      const insumosRows: InsumoRow[] = insumosData.map(
        (ins: FormulaHasInsumo) => ({
          insumoId: String(ins.insumo.idInsumo),
          quantidadeUtilizada: String(ins.quantidadeUtilizada),
        })
      );
      setInsumoRows(insumosRows);
    } catch (err: any) {
      console.error("Erro ao carregar relacionamentos da fórmula", err);
      // Don't set error here, just log it - relationships might not exist yet
    }
  };

  const handleEdit = async (row: any) => {
    const id = row.id ?? row.idFormula;
    setEditingId(id);
    setForm({
      produtoId: row.produto?.idProduto ? String(row.produto.idProduto) : "",
      descricaoModoPreparo: row["Descrição"] ?? row.descricaoModoPreparo ?? "",
    });
    // Load existing relationships
    await loadFormulaRelationships(id);
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir esta fórmula?")) return;

    setError(null);
    try {
      await apiDelete(`/api/formulas/${id}`);
      setFormulas((prev) => prev.filter((f) => f.idFormula !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir fórmula";
      setError(errorMsg);
      console.error("Erro ao excluir fórmula", err);
    }
  };

  const handleAddMateriaPrima = () => {
    setMateriaPrimaRows([
      ...materiaPrimaRows,
      { materiaPrimaId: "", quantidadeUtilizada: "" },
    ]);
  };

  const handleRemoveMateriaPrima = (index: number) => {
    setMateriaPrimaRows(materiaPrimaRows.filter((_, i) => i !== index));
  };

  const handleUpdateMateriaPrima = (
    index: number,
    field: keyof MateriaPrimaRow,
    value: string
  ) => {
    const updated = [...materiaPrimaRows];
    updated[index] = { ...updated[index], [field]: value };
    setMateriaPrimaRows(updated);
  };

  const handleAddInsumo = () => {
    setInsumoRows([...insumoRows, { insumoId: "", quantidadeUtilizada: "" }]);
  };

  const handleRemoveInsumo = (index: number) => {
    setInsumoRows(insumoRows.filter((_, i) => i !== index));
  };

  const handleUpdateInsumo = (
    index: number,
    field: keyof InsumoRow,
    value: string
  ) => {
    const updated = [...insumoRows];
    updated[index] = { ...updated[index], [field]: value };
    setInsumoRows(updated);
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Fórmulas</h1>
          <p>Registre composições e vínculos com produtos.</p>
        </div>
      </header>

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>{editingId ? "Editar fórmula" : "Nova fórmula"}</h2>
            <span className="card-subtitle">
              Associe a fórmula a um produto e descreva a composição.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Produto</label>
                <select
                  value={form.produtoId}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, produtoId: e.target.value }))
                  }
                  required
                >
                  <option value="">Selecione um produto</option>
                  {produtos.map((p) => (
                    <option key={p.idProduto} value={p.idProduto}>
                      {p.nome}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-row" style={{ gridColumn: "1 / -1" }}>
                <label>Descrição do Modo de Preparo</label>
                <textarea
                  value={form.descricaoModoPreparo}
                  onChange={(e) =>
                    setForm((f) => ({
                      ...f,
                      descricaoModoPreparo: e.target.value,
                    }))
                  }
                  placeholder="Ex.: 50% oleo de girassol, 50% agua, 1 frasco 60ml, 1 tampa conta gotas"
                  style={{
                    background: "rgba(15,15,20,0.85)",
                    borderRadius: "10px",
                    border: "1px solid rgba(255,255,255,0.07)",
                    padding: "0.8rem",
                    minHeight: "120px",
                    color: "#fff",
                  }}
                />
              </div>
            </div>

            {/* Matérias-primas section */}
            <div
              style={{
                gridColumn: "1 / -1",
                marginTop: "1.5rem",
                paddingTop: "1.5rem",
                borderTop: "1px solid rgba(255,255,255,0.1)",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  marginBottom: "1rem",
                }}
              >
                <h3 style={{ margin: 0 }}>Matérias-primas</h3>
                <button
                  type="button"
                  onClick={handleAddMateriaPrima}
                  className="btn btn-secondary"
                  style={{ padding: "0.5rem 1rem" }}
                >
                  + Adicionar
                </button>
              </div>
              {materiaPrimaRows.map((row, index) => (
                <div
                  key={index}
                  style={{
                    display: "grid",
                    gridTemplateColumns: "2fr 1fr auto",
                    gap: "1rem",
                    marginBottom: "1rem",
                    alignItems: "end",
                  }}
                >
                  <div className="form-row">
                    <label>Matéria-prima</label>
                    <select
                      value={row.materiaPrimaId}
                      onChange={(e) =>
                        handleUpdateMateriaPrima(
                          index,
                          "materiaPrimaId",
                          e.target.value
                        )
                      }
                      required
                    >
                      <option value="">Selecione</option>
                      {materiasPrimas.map((mp) => (
                        <option
                          key={mp.idMateriaPrima}
                          value={mp.idMateriaPrima}
                        >
                          {mp.nome}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="form-row">
                    <label>Quantidade utilizada</label>
                    <input
                      type="number"
                      step="0.01"
                      min="0"
                      value={row.quantidadeUtilizada}
                      onChange={(e) =>
                        handleUpdateMateriaPrima(
                          index,
                          "quantidadeUtilizada",
                          e.target.value
                        )
                      }
                      placeholder="Ex.: 50.0"
                      required
                    />
                  </div>
                  <button
                    type="button"
                    onClick={() => handleRemoveMateriaPrima(index)}
                    className="btn btn-secondary"
                    style={{ padding: "0.5rem 1rem" }}
                  >
                    Remover
                  </button>
                </div>
              ))}
            </div>

            {/* Insumos section */}
            <div
              style={{
                gridColumn: "1 / -1",
                marginTop: "1.5rem",
                paddingTop: "1.5rem",
                borderTop: "1px solid rgba(255,255,255,0.1)",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  marginBottom: "1rem",
                }}
              >
                <h3 style={{ margin: 0 }}>Insumos</h3>
                <button
                  type="button"
                  onClick={handleAddInsumo}
                  className="btn btn-secondary"
                  style={{ padding: "0.5rem 1rem" }}
                >
                  + Adicionar
                </button>
              </div>
              {insumoRows.map((row, index) => (
                <div
                  key={index}
                  style={{
                    display: "grid",
                    gridTemplateColumns: "2fr 1fr auto",
                    gap: "1rem",
                    marginBottom: "1rem",
                    alignItems: "end",
                  }}
                >
                  <div className="form-row">
                    <label>Insumo</label>
                    <select
                      value={row.insumoId}
                      onChange={(e) =>
                        handleUpdateInsumo(index, "insumoId", e.target.value)
                      }
                      required
                    >
                      <option value="">Selecione</option>
                      {insumos.map((ins) => (
                        <option key={ins.idInsumo} value={ins.idInsumo}>
                          {ins.nome}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="form-row">
                    <label>Quantidade utilizada</label>
                    <input
                      type="number"
                      step="1"
                      min="0"
                      value={row.quantidadeUtilizada}
                      onChange={(e) =>
                        handleUpdateInsumo(
                          index,
                          "quantidadeUtilizada",
                          e.target.value
                        )
                      }
                      placeholder="Ex.: 2"
                      required
                    />
                  </div>
                  <button
                    type="button"
                    onClick={() => handleRemoveInsumo(index)}
                    className="btn btn-secondary"
                    style={{ padding: "0.5rem 1rem" }}
                  >
                    Remover
                  </button>
                </div>
              ))}
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar fórmula"}
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
              <div className="stat-label">Total de fórmulas</div>
              <div className="stat-value">{formulas.length}</div>
            </div>
          </div>
        </section>
      </div>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Fórmulas cadastradas</h2>
        </div>

        {error && (
          <div
            className="error-message"
            style={{
              padding: "1rem",
              margin: "1rem 0",
              background: "#fee",
              border: "1px solid #fcc",
              borderRadius: "4px",
              color: "#c33",
            }}
          >
            {error}
          </div>
        )}

        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : formulas.length === 0 ? (
            <div className="empty-state">Nenhuma fórmula cadastrada.</div>
          ) : (
            <CrudTable
              data={formulas.map((f) => ({
                id: f.idFormula,
                idFormula: f.idFormula,
                Produto: f.produto?.nome ?? "-",
                "Descrição do Modo de Preparo": f.descricaoModoPreparo ?? "-",
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
