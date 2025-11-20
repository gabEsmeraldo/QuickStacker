// src/components/CrudTable.tsx
interface TableProps {
  data: any[];
  onEdit?: (row: any) => void;
  onDelete?: (id: number) => void;
}

export default function CrudTable({ data, onEdit, onDelete }: TableProps) {
  if (!data || !data.length) {
    return <p className="empty-state">Nenhum item cadastrado.</p>;
  }

  const columns = Object.keys(data[0]).filter((k) => k !== "id");

  return (
    <table className="table">
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col}>{col.toUpperCase()}</th>
          ))}
          <th>AÇÕES</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row) => (
          <tr key={row.id}>
            {columns.map((col) => (
              <td key={col}>{row[col]}</td>
            ))}
            <td style={{ whiteSpace: "nowrap" }}>
              {onEdit && (
                <button
                  className="btn btn-outline"
                  style={{ marginRight: "0.4rem" }}
                  onClick={() => onEdit(row)}
                >
                  Editar
                </button>
              )}
              {onDelete && (
                <button
                  className="btn btn-danger"
                  onClick={() => onDelete(row.id)}
                >
                  Excluir
                </button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
