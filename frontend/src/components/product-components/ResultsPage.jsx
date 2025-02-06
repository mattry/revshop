import { Link, useLocation } from "react-router-dom";
import { Box, Typography, List, ListItem, ListItemText } from "@mui/material";

const ResultsPage = () => {
    const location = useLocation();
    const results = location.state?.results || [];

    return (
        <Box sx={{ p: 3 }}>
        <Typography variant="h4">Search Results</Typography>
        {results.length > 0 ? (
          <List>
            {results.map((product) => (
              <ListItem key={product.productId} button="true" component={Link} to={`/product/${product.productId}`}>
                <ListItemText primary={product.name} secondary={`Price: $${product.price.toFixed(2)}`}/>
              </ListItem>
            ))}
          </List>
        ) : (
          <Typography>No results found.</Typography>
        )}
      </Box>
    );

}

export default ResultsPage;