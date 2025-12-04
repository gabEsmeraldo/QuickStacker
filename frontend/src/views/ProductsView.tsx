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

interface Lote {
  idLote: number;
  dataValidade: string;
  dataProducao: string;
  custoUnitarioProducao: number | null;
  quantidade: number;
  produto: {
    idProduto: number;
    nome: string;
  };
}

export default function ProductsView() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [categorias, setCategorias] = useState<
    Array<{ idCategoria: number; descricao: string }>
  >([]);
  const [form, setForm] = useState({
    nome: "",
    validadeEmMeses: "",
    categoriaId: "",
  });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Lote management state
  const [selectedProdutoId, setSelectedProdutoId] = useState<number | null>(
    null
  );
  const [lotes, setLotes] = useState<Lote[]>([]);
  const [loteForm, setLoteForm] = useState({
    dataProducao: "",
    dataValidade: "",
    quantidade: "",
    custoUnitarioProducao: "",
  });
  const [editingLoteId, setEditingLoteId] = useState<number | null>(null);
  const [loadingLotes, setLoadingLotes] = useState(false);
  
  // Categoria management state
  const [categoriaForm, setCategoriaForm] = useState({ descricao: "" });
  const [showCategoriaForm, setShowCategoriaForm] = useState(false);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const [produtosData, categoriasData] = await Promise.all([
          apiGet("/api/produtos"),
          apiGet("/api/categorias"),
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
      validadeEmMeses: form.validadeEmMeses
        ? parseInt(form.validadeEmMeses)
        : null,
      categoria: {
        idCategoria: parseInt(form.categoriaId),
      },
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
      categoriaId: row.categoria?.idCategoria
        ? String(row.categoria.idCategoria)
        : "",
    });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir este produto?")) return;

    setError(null);
    try {
      await apiDelete(`/api/produtos/${id}`);
      setProdutos((prev) => prev.filter((p) => p.idProduto !== id));
      if (selectedProdutoId === id) {
        setSelectedProdutoId(null);
        setLotes([]);
      }
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir produto";
      setError(errorMsg);
      console.error("Erro ao excluir produto", err);
    }
  };

  // Lote management functions
  const loadLotes = async (produtoId: number) => {
    setLoadingLotes(true);
    setError(null);
    try {
      const lotesData = await apiGet(`/api/lotes/produto/${produtoId}`);
      setLotes(lotesData);
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao carregar lotes";
      setError(errorMsg);
      console.error("Erro ao carregar lotes", err);
    } finally {
      setLoadingLotes(false);
    }
  };

  const handleProdutoSelect = (produtoId: number | null) => {
    setSelectedProdutoId(produtoId);
    if (produtoId) {
      loadLotes(produtoId);
    } else {
      setLotes([]);
    }
    setLoteForm({
      dataProducao: "",
      dataValidade: "",
      quantidade: "",
      custoUnitarioProducao: "",
    });
    setEditingLoteId(null);
  };

  const calculateDataValidade = (
    dataProducao: string,
    validadeEmMeses: number | null
  ): string => {
    if (!dataProducao || !validadeEmMeses) return "";
    const data = new Date(dataProducao);
    data.setMonth(data.getMonth() + validadeEmMeses);
    return data.toISOString().split("T")[0];
  };

  const handleLoteSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!selectedProdutoId) {
      setError("Selecione um produto primeiro");
      return;
    }

    const produto = produtos.find((p) => p.idProduto === selectedProdutoId);
    if (!produto) {
      setError("Produto não encontrado");
      return;
    }

    // Auto-calculate dataValidade if not provided
    let dataValidade = loteForm.dataValidade;
    if (!dataValidade && loteForm.dataProducao && produto.validadeEmMeses) {
      dataValidade = calculateDataValidade(
        loteForm.dataProducao,
        produto.validadeEmMeses
      );
    }

    const payload = {
      dataProducao: loteForm.dataProducao,
      dataValidade: dataValidade,
      quantidade: parseInt(loteForm.quantidade),
      custoUnitarioProducao: loteForm.custoUnitarioProducao
        ? parseFloat(loteForm.custoUnitarioProducao)
        : null,
      produto: {
        idProduto: selectedProdutoId,
      },
    };

    setError(null);
    try {
      if (editingLoteId) {
        const updated = await apiPut(`/api/lotes/${editingLoteId}`, payload);
        setLotes((prev) =>
          prev.map((l) => (l.idLote === editingLoteId ? updated : l))
        );
        setEditingLoteId(null);
      } else {
        const created = await apiPost("/api/lotes", payload);
        setLotes((prev) => [...prev, created]);
      }
      setLoteForm({
        dataProducao: "",
        dataValidade: "",
        quantidade: "",
        custoUnitarioProducao: "",
      });
      // Reload produtos to update quantidadeTotal
      const produtosData = await apiGet("/api/produtos");
      setProdutos(produtosData);
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar lote";
      setError(errorMsg);
      console.error("Erro ao salvar lote", err);
    }
  };

  const handleEditLote = (lote: Lote) => {
    setEditingLoteId(lote.idLote);
    setLoteForm({
      dataProducao: lote.dataProducao.split("T")[0],
      dataValidade: lote.dataValidade.split("T")[0],
      quantidade: String(lote.quantidade),
      custoUnitarioProducao: lote.custoUnitarioProducao
        ? String(lote.custoUnitarioProducao)
        : "",
    });
  };

  const handleDeleteLote = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir este lote?")) return;

    setError(null);
    try {
      await apiDelete(`/api/lotes/${id}`);
      setLotes((prev) => prev.filter((l) => l.idLote !== id));
      // Reload produtos to update quantidadeTotal
      const produtosData = await apiGet("/api/produtos");
      setProdutos(produtosData);
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir lote";
      setError(errorMsg);
      console.error("Erro ao excluir lote", err);
    }
  };

  const handleLoteDataProducaoChange = (value: string) => {
    setLoteForm((f) => {
      const produto = produtos.find((p) => p.idProduto === selectedProdutoId);
      const newForm = { ...f, dataProducao: value };
      if (produto?.validadeEmMeses && !editingLoteId) {
        // Auto-calculate dataValidade when creating new lote
        newForm.dataValidade = calculateDataValidade(
          value,
          produto.validadeEmMeses
        );
      }
      return newForm;
    });
  };

  // Categoria management functions
  const handleCreateCategoria = async () => {
    if (!categoriaForm.descricao.trim()) {
      setError("Digite o nome da categoria");
      return;
    }

    setError(null);
    try {
      const created = await apiPost("/api/categorias", {
        descricao: categoriaForm.descricao.trim(),
      });
      setCategorias((prev) => [...prev, created]);
      setForm((f) => ({ ...f, categoriaId: String(created.idCategoria) }));
      setCategoriaForm({ descricao: "" });
      setShowCategoriaForm(false);
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao criar categoria";
      setError(errorMsg);
      console.error("Erro ao criar categoria", err);
    }
  };

  const handleDeleteCategoria = async (id: number) => {
    // Check if categoria has produtos
    const produtosInCategoria = produtos.filter(
      (p) => p.categoria?.idCategoria === id
    );

    if (produtosInCategoria.length > 0) {
      const confirmMsg = `Esta categoria possui ${produtosInCategoria.length} produto(s) associado(s). Deseja realmente excluir? Isso pode causar problemas com os produtos existentes.`;
      if (!confirm(confirmMsg)) return;
    } else {
      if (!confirm("Tem certeza que deseja excluir esta categoria?")) return;
    }

    setError(null);
    try {
      await apiDelete(`/api/categorias/${id}`);
      setCategorias((prev) =>
        prev.filter((c) => c.idCategoria !== id)
      );
      // Clear categoria selection if it was the deleted one
      if (form.categoriaId === String(id)) {
        setForm((f) => ({ ...f, categoriaId: "" }));
      }
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
          <h1>Produtos</h1>
          <p>Cadastre e gerencie os produtos finais da indústria.</p>
        </div>
      </header>

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
                <label>
                  Categoria
                  <button
                    type="button"
                    onClick={() => setShowCategoriaForm(!showCategoriaForm)}
                    className="btn btn-secondary"
                    style={{
                      marginLeft: "0.5rem",
                      padding: "0.25rem 0.5rem",
                      fontSize: "0.8rem",
                    }}
                  >
                    {showCategoriaForm ? "−" : "+"} Nova
                  </button>
                </label>
                {showCategoriaForm ? (
                  <div
                    style={{
                      display: "flex",
                      gap: "0.5rem",
                      alignItems: "flex-end",
                    }}
                  >
                    <input
                      type="text"
                      value={categoriaForm.descricao}
                      onChange={(e) =>
                        setCategoriaForm({ descricao: e.target.value })
                      }
                      placeholder="Nome da categoria"
                      style={{ flex: 1 }}
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
                          e.preventDefault();
                          handleCreateCategoria();
                        }
                      }}
                    />
                    <button
                      type="button"
                      onClick={handleCreateCategoria}
                      className="btn btn-primary"
                      disabled={!categoriaForm.descricao.trim()}
                    >
                      Criar
                    </button>
                    <button
                      type="button"
                      onClick={() => {
                        setShowCategoriaForm(false);
                        setCategoriaForm({ descricao: "" });
                      }}
                      className="btn btn-secondary"
                    >
                      Cancelar
                    </button>
                  </div>
                ) : (
                  <div style={{ display: "flex", gap: "0.5rem" }}>
                    <select
                      value={form.categoriaId}
                      onChange={(e) =>
                        setForm((f) => ({ ...f, categoriaId: e.target.value }))
                      }
                      required
                      style={{ flex: 1 }}
                    >
                      <option value="">Selecione uma categoria</option>
                      {categorias.map((cat) => (
                        <option key={cat.idCategoria} value={cat.idCategoria}>
                          {cat.descricao}
                        </option>
                      ))}
                    </select>
                    {form.categoriaId && (
                      <button
                        type="button"
                        onClick={() => handleDeleteCategoria(parseInt(form.categoriaId))}
                        className="btn btn-danger"
                        title="Excluir categoria"
                        style={{ padding: "0.5rem" }}
                      >
                        ×
                      </button>
                    )}
                  </div>
                )}
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

      {/* Lote Management Section */}
      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Gerenciar Lotes</h2>
          <span className="card-subtitle">
            Selecione um produto para visualizar e gerenciar seus lotes.
          </span>
        </div>

        <div style={{ marginBottom: "1.5rem" }}>
          <label style={{ display: "block", marginBottom: "0.5rem" }}>
            Selecione um produto
          </label>
          <select
            value={selectedProdutoId || ""}
            onChange={(e) =>
              handleProdutoSelect(
                e.target.value ? parseInt(e.target.value) : null
              )
            }
            style={{
              width: "100%",
              padding: "0.75rem",
              background: "rgba(15,15,20,0.85)",
              border: "1px solid rgba(255,255,255,0.1)",
              borderRadius: "8px",
              color: "#fff",
            }}
          >
            <option value="">Selecione um produto</option>
            {produtos.map((p) => (
              <option key={p.idProduto} value={p.idProduto}>
                {p.nome}
              </option>
            ))}
          </select>
        </div>

        {selectedProdutoId && (
          <>
            <form
              onSubmit={handleLoteSubmit}
              style={{
                marginBottom: "1.5rem",
                padding: "1rem",
                background: "rgba(255,255,255,0.03)",
                borderRadius: "8px",
              }}
            >
              <h3 style={{ marginTop: 0, marginBottom: "1rem" }}>
                {editingLoteId ? "Editar Lote" : "Novo Lote"}
              </h3>
              <div className="form-grid">
                <div className="form-row">
                  <label>Data de Produção</label>
                  <input
                    type="date"
                    value={loteForm.dataProducao}
                    onChange={(e) =>
                      handleLoteDataProducaoChange(e.target.value)
                    }
                    required
                  />
                </div>
                <div className="form-row">
                  <label>
                    Data de Validade
                    {!editingLoteId && (
                      <span
                        style={{
                          fontSize: "0.8rem",
                          color: "rgba(255,255,255,0.6)",
                          marginLeft: "0.5rem",
                        }}
                      >
                        (calculada automaticamente)
                      </span>
                    )}
                  </label>
                  <input
                    type="date"
                    value={loteForm.dataValidade}
                    onChange={(e) =>
                      setLoteForm((f) => ({
                        ...f,
                        dataValidade: e.target.value,
                      }))
                    }
                    required
                    disabled={
                      !editingLoteId &&
                      !!produtos.find((p) => p.idProduto === selectedProdutoId)
                        ?.validadeEmMeses
                    }
                    title={
                      !editingLoteId
                        ? "Calculada automaticamente baseada na validade do produto"
                        : ""
                    }
                  />
                </div>
                <div className="form-row">
                  <label>Quantidade</label>
                  <input
                    type="number"
                    min="1"
                    value={loteForm.quantidade}
                    onChange={(e) =>
                      setLoteForm((f) => ({ ...f, quantidade: e.target.value }))
                    }
                    required
                  />
                </div>
                <div className="form-row">
                  <label>Custo Unitário de Produção</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={loteForm.custoUnitarioProducao}
                    onChange={(e) =>
                      setLoteForm((f) => ({
                        ...f,
                        custoUnitarioProducao: e.target.value,
                      }))
                    }
                  />
                </div>
              </div>
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingLoteId ? "Salvar alterações" : "Cadastrar lote"}
                </button>
                {editingLoteId && (
                  <button
                    type="button"
                    onClick={() => {
                      setEditingLoteId(null);
                      setLoteForm({
                        dataProducao: "",
                        dataValidade: "",
                        quantidade: "",
                        custoUnitarioProducao: "",
                      });
                    }}
                    className="btn btn-secondary"
                  >
                    Cancelar
                  </button>
                )}
              </div>
            </form>

            <div>
              <h3 style={{ marginBottom: "1rem" }}>Lotes do Produto</h3>
              {loadingLotes ? (
                <div className="empty-state">Carregando lotes...</div>
              ) : lotes.length === 0 ? (
                <div className="empty-state">
                  Nenhum lote cadastrado para este produto.
                </div>
              ) : (
                <CrudTable
                  data={lotes.map((l) => ({
                    id: l.idLote,
                    idLote: l.idLote,
                    "Data de Produção": l.dataProducao.split("T")[0],
                    "Data de Validade": l.dataValidade.split("T")[0],
                    Quantidade: l.quantidade,
                    "Custo Unitário": l.custoUnitarioProducao ?? "-",
                  }))}
                  onEdit={handleEditLote}
                  onDelete={handleDeleteLote}
                />
              )}
            </div>
          </>
        )}
      </section>
    </>
  );
}
