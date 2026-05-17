## Sự khác biệt giữa HTTP POST và HTTP PUT

### HTTP POST

Phương thức `POST` thường được sử dụng để tạo mới tài nguyên trên server.

Ví dụ:

- Tạo tài khoản khách hàng mới.
- Thêm đơn hàng mới.
- Tạo sản phẩm mới.

Đặc điểm của `POST`:

- Không có tính chất **idempotent**.
- Nếu gửi cùng một request nhiều lần, server có thể tạo ra nhiều tài nguyên mới khác nhau.

Ví dụ:

```http
POST /api/customers
```

Nếu người dùng bấm nút gửi nhiều lần hoặc request bị gửi lại, hệ thống có thể tạo:

- Customer #1
- Customer #2
- Customer #3

=> Dẫn đến dữ liệu bị trùng lặp.

---

### HTTP PUT

Phương thức `PUT` thường được sử dụng để cập nhật tài nguyên đã tồn tại.

Ví dụ:

- Cập nhật tên khách hàng.
- Cập nhật email.
- Cập nhật thông tin hồ sơ.

Đặc điểm của `PUT`:

- Có tính chất **idempotent**.
- Gửi cùng một request nhiều lần vẫn cho ra cùng một kết quả.

Ví dụ:

```http
PUT /api/customers/1
```

Nếu gửi request cập nhật cùng dữ liệu 10 lần:

- Customer có ID = 1 vẫn chỉ là một bản ghi duy nhất.
- Dữ liệu cuối cùng không thay đổi thêm.

=> Không tạo dữ liệu trùng lặp.

---

## Tại sao dùng POST cho cả tạo mới và cập nhật dễ gây lỗi dữ liệu trùng lặp?

Trong đoạn mã đã cho, API sử dụng:

```java

@PostMapping
public ResponseEntity<Customer> createOrUpdateCustomer(...)
```

để xử lý cả:

- tạo mới khách hàng
- cập nhật khách hàng

Điều này gây ra vấn đề vì `POST` vốn được thiết kế cho việc tạo mới tài nguyên.

---

## Lỗi logic trong đoạn code

Khi client gửi dữ liệu có `id`, hệ thống sẽ tìm khách hàng tương ứng:

```java
Optional<Customer> existingCustomer = customers.stream()
        .filter(c -> c.getId().equals(customer.getId()))
        .findFirst();
```

Nếu tìm thấy:

- cập nhật dữ liệu.

Nhưng nếu không tìm thấy:

```java
customers.add(customer);
return new ResponseEntity<>(customer,HttpStatus.CREATED);
```

hệ thống lại tạo mới khách hàng.

Điều này vi phạm quy tắc nghiệp vụ:

- Cập nhật phải chỉ sửa dữ liệu đã tồn tại.
- Không được tạo thêm bản ghi mới khi cập nhật sai ID.

---

## Hậu quả thực tế

Khi người dùng:

- bấm nút cập nhật nhiều lần,
- mạng chậm khiến request gửi lại,
- frontend gửi sai ID,
- hoặc request bị retry tự động,

API dùng `POST` có thể:

- tạo thêm customer mới,
- gây trùng lặp dữ liệu,
- làm mất tính nhất quán của hệ thống.

Ví dụ:

- Đáng lẽ cập nhật Customer ID = 5
- Nhưng do lỗi request không tìm thấy ID
- Hệ thống lại tạo thêm Customer mới ID = 5 hoặc dữ liệu tương tự.

---

## Kết luận

- `POST` phù hợp cho tạo mới tài nguyên.
- `PUT` phù hợp cho cập nhật tài nguyên đã tồn tại.
- `PUT` có tính chất idempotent nên an toàn hơn khi request bị gửi lặp.
- Việc dùng `POST` cho cả tạo mới và cập nhật dễ dẫn đến:
    - tạo dữ liệu trùng lặp,
    - mất tính nhất quán,
    - sai nghiệp vụ RESTful API.