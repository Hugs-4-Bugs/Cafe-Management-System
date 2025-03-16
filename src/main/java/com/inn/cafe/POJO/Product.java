package com.inn.cafe.POJO;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;



@NamedQuery(name = "Product.getAllProduct", query = "select new com.inn.cafe.Wrapper.ProductWrapper(p.id, p.name, " + "p.description, p.price, p.status,  p.category.id, p.category.name) from Product p")
@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status = :status where p.id = :id")
// word status in this query should be same as written in "private String status;", if you write "private String Status;" then also first letter of status should be capital in query as 'Status' not 'status'
@NamedQuery(name = "Product.getProductByCategory", query = "select new com.inn.cafe.Wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id = :id and p.status = 'true' ")  // p.category.id is actually p.categoryId
                                                                                              // p.id, p.name are the selected column name [i.e. id & name column is selected]
@NamedQuery(name = "Product.getProductById", query = "select new com.inn.cafe.Wrapper.ProductWrapper(p.id, p.name, p.description, p.price) from Product p where p.id = :id")

/** This above query fetches product details (id, name, description, price, status)
 along with associated category details (category id and category name),
 and maps them to the ProductWrapper class for optimized API responses.

 AND Order of p.id, p.name, p.description, p.price, p.status,  p.category.id, p.category.name should be same as you
 have generated the constructor in the ProductWrapper class otherwise it will assign the value in wrong row & column
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable {

    public static final Long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    /** because one category have many products (category -> products).
        and fetch type LAZY will load the date on demand increasing the application performance
        means here in this case if you select the category then it will load the category otherwise not
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)    // category_fk = category foreign key
    private Category category;


    /** we are using the Wrapper class here (ex, String, Integer instead of int, Str etc.)
     * which is used to convert the primitive datatype into object.*/
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;

}
