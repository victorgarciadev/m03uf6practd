package dades;

import entitats.Producte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de Productes a la BBDD.
 *
 * @author Pablo Morante - Creació
 * @author Victor García - Creació
 * @author Izan Jimenez - Creació/Implementació
 */
public class ProducteDAO extends DataLayer implements DAOInterface<Producte> {

    //comandes SQL
    final String INSERT = "INSERT INTO products(productName, productDescription, quantityInStock, buyPrice) VALUES (?, ?, ?, ?)";
    final String UPDATE = "UPDATE products SET productName = ?, productDescription = ?, quantityInStock = ?, buyPrice = ? WHERE productCode = ?";
    final String DELETE = "DELETE FROM products WHERE productCode = ?";
    final String GETALL = "SELECT * FROM products";
    final String GETONE = "SELECT * FROM products WHERE productCode = ?";

    //al crear un ProducteDAO crea una conexió 
    public ProducteDAO() throws SQLException {
        super();
    }

    /**
     * Desar un producte a la taula 'products' (RF58)
     *
     * @param t Instància de tipus Producte a desar
     * @author Izan Jimenez - Implementació
     */
    @Override
    public void save(Producte t) {
        //es prepara el Statement
        System.out.println("Guardant producte");
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            //es carrega el statement amb la conexió i la comanda
            stat = super.getCon().prepareStatement(INSERT);
            //inserim els camps del producte al statement
            stat.setString(1, t.getProductName());
            stat.setString(2, t.getProductDescription());
            stat.setInt(3, t.getQuantityInStock());
            stat.setFloat(4, t.getBuyPrice());

            //si executeUpdate torna 0, no s'ha afegit/modificat cap fila a a BBDD
            if (stat.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("ERROR AL GUARDAR PRODUCTE: " + e);
        } finally {
            //tanquem el statement
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR STAT " + e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR RS: " + e);
                }
            }
        }
    }

    /**
     * Modifica un producte a la taula 'products' (RF62)
     *
     * @param t Instància de tipus Producte a modificar
     * @author Izan Jimenez - Implementació
     */
    @Override
    public void update(Producte t) {
        System.out.println("Modificant producte");
        PreparedStatement stat = null;
        try {
            //es carrega el statement amb la conexió i la comanda
            stat = super.getCon().prepareStatement(UPDATE);
            //inserim els camps del producte al statement
            stat.setString(1, t.getProductName());
            stat.setString(2, t.getProductDescription());
            stat.setInt(3, t.getQuantityInStock());
            stat.setFloat(4, t.getBuyPrice());
            stat.setInt(5, t.getProductCode());

            //si executeUpdate torna 0, no s'ha afegit/modificat cap fila a a BBDD
            if (stat.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("ERROR AL GUARDAR PRODUCTE: " + e);
        } finally {
            //tanquem el statement
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR STAT: " + e);
                }

            }
        }
    }

    /**
     * Elimina un producte a la taula 'products' (RF60)
     *
     * @param t Instància de tipus Producte a eliminar
     * @author Izan Jimenez - Implementació
     */
    @Override
    public void delete(Producte t) {
        PreparedStatement stat = null;
        System.out.println("Eliminat producte producte: " + t.getProductCode());

        try {
            stat = super.getCon().prepareStatement(DELETE);
            stat.setInt(1, t.getProductCode());
            if (stat.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("ERROR EN ELIMINAR PRODUCTE: " + e);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR STAT EN ELIMINAR PRODUCTE: " + e);
                }
            }
        }
    }

    /**
     * Recupera un producte de la taula 'products'
     *
     * @param t Instància de tipus Producte a recuperar
     * @return Instància de tipus Producte amb totes les dades recuperades de la
     * BD
     * @author Izan Jimenez - Implementació
     */
    @Override
    public Producte getOne(Producte t) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Producte p = null;

        try {

            stat = super.getCon().prepareStatement(GETONE);
            stat.setInt(1, t.getProductCode());
            rs = stat.executeQuery();
//          si rep algun parametre el transformem a Porducte
            if (rs.next()) {
                p = convertir(rs);
            } else {
                System.out.println("No s'ha trobat aquest registre");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("Error al obtenir un producte: " + e);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR STAT: " + e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR RS: " + e);
                }

            }
        }
        return p;

    }

    /**
     * Mostrar tots els productes de la taula 'productes' (RF64)
     *
     * @return List de tipus Producte amb tots els registres de la BD
     * @author Izan Jimenez - Implementació
     */
    @Override
    public List<Producte> getAll() {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Producte> p = new ArrayList<>();

        try {
            stat = super.getCon().prepareStatement(GETALL);
            rs = stat.executeQuery();
            while (rs.next()) {
                p.add(convertir(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtenir un producte: " + e);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR STAT: " + e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL TANCAR RS: " + e);
                }

            }
        }
        return p;
    }

    /**
     * *Transforma un Resulset no null a un instancia Producte
     *
     * @param rs
     * @return Retorna una instancia Producte del ResulSet
     * @throws SQLException
     * @author Izan JImenez - Implementació
     */
    private Producte convertir(ResultSet rs) throws SQLException {
//      agafem els atributs
        String nomProducte = rs.getString("productName");
        String descProducte = rs.getString("productDescription");
        int stock = rs.getInt("quantityInStock");
        float preu = rs.getFloat("buyPrice");
//      retornem una instanci aproducte
        Producte p = new Producte(nomProducte, descProducte, stock, preu);
        p.setProductCode(rs.getInt("productCode"));

        return p;
    }

    
    
//    public static void main(String[] args) {
//        try {
//            ProducteDAO pd = new ProducteDAO();
//
//            //SAVE
////            Producte p = new Producte("Gafas", "Gafas2", 2, (float) 5.99);
////            pd.save(p);
//            //GetOne
//            Producte p = new Producte("GAFITAS", "GAFITAS MODIFICADAS", 0, 0);
//            p.setProductCode(3);
//            System.out.println("---" + pd.getOne(p));
//            //UPDATE
////            pd.update(p);
//            //DELETE
////            pd.delete(p);
//            //GETALL
//            List<Producte> productes = pd.getAll();
//            for (Producte producte : productes) {
//                System.out.println(producte.toString());
//            }
//        } catch (Exception e) {
//        } finally {
//        }
//    }
}
