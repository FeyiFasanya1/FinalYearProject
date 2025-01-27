    package com.example.app.Helper;

    import android.content.Context;
    import android.util.Log;
    import android.widget.Toast;

    import com.example.app.Domain.ItemsDomain;

    import java.util.ArrayList;

    public class ManagmentCart {

        private Context context;
        private TinyDB tinyDB;

        public ManagmentCart(Context context) {
            this.context = context;
            this.tinyDB = new TinyDB(context);
        }

        public void insertItem(ItemsDomain item) {
            ArrayList<ItemsDomain> listfood = getListCart();
            boolean existAlready = false;
            int n = 0;
            for (int y = 0; y < listfood.size(); y++) {
                if (listfood.get(y).getTitle().equals(item.getTitle()) &&
                        listfood.get(y).getSelectedSize().equals(item.getSelectedSize())) {
                    existAlready = true;
                    n = y;
                    break;
                }
            }
            if (existAlready) {
                listfood.get(n).setNumberinCart(listfood.get(n).getNumberinCart() + item.getNumberinCart());
            } else {
                listfood.add(item);
            }

            Log.e("Item Added", item.getTitle() + ", " + item.getQuantity().getSmall());
            tinyDB.putListObject("CartList", listfood);
            Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
        }


        public ArrayList<ItemsDomain> getListCart() {
            return tinyDB.getListObject("CartList");
        }

        public void minusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
            if (listfood.get(position).getNumberinCart() == 1) {
                listfood.remove(position);
            } else {
                listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() - 1);
            }
            tinyDB.putListObject("CartList", listfood);
            changeNumberItemsListener.changed();
        }

        public void plusItem(ArrayList<ItemsDomain> listfood, int position, String size, ChangeNumberItemsListener changeNumberItemsListener) {
            ItemsDomain itemDomain = listfood.get(position);
            int itemCount = itemDomain.getNumberinCart();
            Log.d("ITEM COUNT", String.valueOf(itemCount));
            int itemQuantity = itemDomain.getQuantity().getQuantityForSize(size);
            Log.d("ORDER QUANTITY", String.valueOf(itemQuantity));
            if (itemQuantity < itemCount +1) {
                Log.d("Cart", "Insufficient stock for " + itemDomain.getTitle());
                Toast.makeText(context, "Insufficient stock for " + itemDomain.getTitle(), Toast.LENGTH_SHORT).show();
                return;
            }
            listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() + 1);
            tinyDB.putListObject("CartList", listfood);
            changeNumberItemsListener.changed();
        }


        public Double getTotalFee() {
            ArrayList<ItemsDomain> listfood2 = getListCart();
            double fee = 0;
            for (int i = 0; i < listfood2.size(); i++) {
                fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberinCart());
            }
            return fee;
        }

        // clear the cart
        public void clearCart() {
            tinyDB.putListObject("CartList", new ArrayList<ItemsDomain>());
            Toast.makeText(context, "Cart has been cleared", Toast.LENGTH_SHORT).show();
        }
    }
