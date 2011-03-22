import hr.helix.monadologie.*
import static hr.helix.monadologie.MonadComprehension.foreach
import hr.helix.monadologie.monads.Option
import static hr.helix.monadologie.monads.Option.some
import static hr.helix.monadologie.monads.Option.none


class Order { Option<LineItem> lineItem }
class LineItem { Option<Product> product }
class Product { String name }

def prod =
    /*none()*/
    some(new Product(name:'a new product'))
def maybeOrder = new Order(
        lineItem: some(
            new LineItem( product: prod)
        )
)
/*maybeOrder.lineItem = none()*/

res = foreach {
    order    = takeFrom { some(maybeOrder) }
    lineItem = takeFrom { order.lineItem }
    product  = takeFrom { lineItem.product }
    yield { product.name }
}
println "example2: res=$res"
