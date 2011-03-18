import hr.helix.monadologie.*
import static hr.helix.monadologie.MonadComprehension.foreach
import hr.helix.monadologie.monads.Option
import static hr.helix.monadologie.monads.Option.Some
import static hr.helix.monadologie.monads.Option.None


class Order { Option<LineItem> lineItem }
class LineItem { Option<Product> product }
class Product { String name }

def prod =
    /*None()*/
    Some(new Product(name:'a new product'))
def maybeOrder = new Order(
        lineItem: Some(
            new LineItem( product: prod)
        )
)
/*maybeOrder.lineItem = None()*/

res = foreach {
    order    = takeFrom { Some(maybeOrder) }
    lineItem = takeFrom { order.lineItem }
    product  = takeFrom { lineItem.product }
    yield { product.name }
}
println "example2: res=$res"
