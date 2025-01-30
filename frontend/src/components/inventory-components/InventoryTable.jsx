import { DataGrid, GridDeleteIcon } from '@mui/x-data-grid';
import Paper from '@mui/material/Paper';
import { IconButton } from '@mui/material';
import { Link } from 'react-router-dom';

const paginationModel = { page: 0, pageSize: 5 };

const InventoryTable = ({ inventory, handleDelete }) => {

    const columns = [
        { field: 'id', headerName: 'ID', width: 70 },
        { 
            field: 'productName', 
            headerName: 'Product Name', 
            width: 200,
            renderCell: (params) => (
                <Link to={`/product/${params.row.id}`} style={{textDecoration: "none", color: "blue"}}>
                    {params.row.productName}
                </Link>
            )
        },
        { field: 'quantity', headerName: 'Quantity', type: 'number', width: 120 },
        { 
            field: 'actions',
            headerName: 'Delete Item',
            width: 100,
            sortable: false,
            renderCell: (params) => (
                <IconButton color="error" onClick={() => handleDelete(params.row.id)}>
                    <GridDeleteIcon />
                </IconButton>
            )
        }
      ];

    return (
        <Paper sx={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={inventory}
                columns={columns}
                initialState={{ pagination: { paginationModel } }}
                pageSizeOptions={[5, 10]}
                checkboxSelection
                sx={{ border: 0 }}
            />
        </Paper>
    );
};

export default InventoryTable;