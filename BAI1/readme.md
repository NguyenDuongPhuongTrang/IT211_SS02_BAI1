Nguyên nhân khiến các ứng dụng client không thể phân tích cú pháp dữ liệu từ Web Service `getHotProducts` nằm ở cách dữ
liệu được trả về trong phương thức:

```java
return products.toString();
```

Mặc dù danh sách `products` đã được thêm các đối tượng `Product`, nhưng phương thức `toString()` của `List<Product>`
không tạo ra dữ liệu ở định dạng JSON hợp lệ. Thay vào đó, nó chỉ chuyển đổi danh sách thành một chuỗi văn bản thông
thường dựa trên phương thức `toString()` mặc định của Java.

Ví dụ dữ liệu trả về có thể giống như:

```text
[Product@1a2b3c, Product@4d5e6f]
```

hoặc:

```text
[com.example.Product@7adf9f5f]
```

Đây không phải là JSON chuẩn nên các ứng dụng frontend/mobile không thể parse dữ liệu để chuyển thành object sử dụng
trong chương trình.

Trong RESTful API, client thường mong đợi dữ liệu ở định dạng JSON như:

```json
[
  {
    "id": "HP001",
    "name": "Áo thun 'Code is Life'",
    "price": 199000
  },
  {
    "id": "HP002",
    "name": "Móc khóa 'Bug Free'",
    "price": 99000
  }
]
```

Tuy nhiên, do phương thức hiện tại trả về kiểu `String`, Spring Boot hiểu rằng lập trình viên muốn trả về chuỗi văn bản
thuần (plain text) thay vì dữ liệu object.

Spring Boot với annotation `@RestController` có cơ chế tự động sử dụng thư viện Jackson để chuyển đổi object Java thành
JSON. Nhưng cơ chế này chỉ hoạt động khi trả về object hoặc collection object như:

```java
List<Product>
```

chứ không hoạt động đúng khi lập trình viên tự chuyển object sang chuỗi bằng `toString()`.

Vì vậy, vấn đề gốc rễ là:

- Dữ liệu được trả về dưới dạng chuỗi text không đúng chuẩn JSON.
- Sử dụng `products.toString()` làm mất khả năng tự động serialize của Spring Boot.
- Client không thể parse response thành JSON object hợp lệ.
- API hoạt động nhưng response không đúng định dạng RESTful API mong muốn.