# Order Tracking - Quick Start Guide

## 🎯 What's New

Admin có thể giả lập giao hàng bằng cách cập nhật tracking status từ trang quản lý mà không cần tích hợp bên giao hàng thực tế.

## 📋 Usage Guide

### Admin: Quản Lý Tracking

#### 1. Truy Cập Trang Quản Lý
- URL: `/admin/orders/{ORDER_ID}/tracking`
- Ví dụ: `/admin/orders/123/tracking`
- Yêu cầu: Must be logged in as ADMIN

#### 2. Cập Nhật Tracking Status
Trong form "Update Tracking" bên phải:

**Step 1: Chọn Status**
```
- WAITING_FOR_PROCESSING → Chờ xử lý
- PROCESSING → Đang xử lý  
- READY_TO_SHIP → Sẵn sàng gửi
- IN_TRANSIT → Đang vận chuyển
- OUT_FOR_DELIVERY → Đang giao
- DELIVERED → Đã giao
```

**Step 2: Nhập Location**
Ví dụ:
```
- Guangzhou Warehouse
- Shanghai Distribution Center
- Beijing Sorting Facility
- On the way to Customer
```

**Step 3: Thêm Description (tùy chọn)**
```
Ví dụ:
- Package is being processed at warehouse
- Loaded onto delivery truck
- Out for delivery today
- Successfully delivered
```

**Step 4: Click "Update Tracking"**
- Hệ thống tự động lưu tracking step mới
- Step trước được đánh dấu là không còn "current"
- Trang tự động reload để hiển thị update

### Customer: Xem Tracking

#### Trên Order Detail Page
- Hiển thị **Current Status** với hightlight
- Timeline của tất cả tracking steps
- Sorted từ mới nhất đến cũ nhất

#### Thông Tin Hiển Thị
- 🚚 **Status**: Current delivery status
- 📍 **Location**: Where the package is
- 📝 **Description**: Detailed information
- 🕐 **Timestamp**: Khi status được cập nhật

## 🔧 API Endpoints

### Get Current Tracking (Public)
```bash
GET /admin/api/tracking/{orderId}/current
```
Response:
```json
{
  "trackingId": 1,
  "orderId": 123,
  "status": "IN_TRANSIT",
  "location": "Shanghai Hub",
  "description": "Package is in transit",
  "isCurrent": true,
  "createdAt": "2026-04-08T14:30:00"
}
```

### Get All Tracking History (Admin Only)
```bash
GET /admin/api/tracking/{orderId}
Authorization: Bearer {JWT_TOKEN}
```

### Update Tracking (Admin Only)
```bash
POST /admin/api/tracking/update/{orderId}
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "status": "IN_TRANSIT",
  "location": "Shanghai Distribution",
  "description": "Package on the way"
}
```

## 🎨 UI Features

### Admin Page Components
1. **Order Information Card**
   - Order ID, Status, Payment Status
   - Created date

2. **Tracking History Timeline**
   - Visual timeline of all updates
   - Dots indicate each step
   - Current status highlighted

3. **Update Form** (Sticky sidebar)
   - Status dropdown
   - Location input
   - Description textarea
   - Submit button

### Customer View Components
1. **Current Status Badge** (Highlighted)
   - Status name
   - Location
   - Latest timestamp

2. **Tracking Timeline**
   - Scrollable history
   - All previous statuses
   - Time indicators

3. **Progress Bar**
   - Visual completion percentage
   - Updated based on order status

## 📱 Responsive Design

- ✅ Desktop: Full layout with sidebar
- ✅ Tablet: Optimized spacing
- ✅ Mobile: Stacked layout with scrolling

## 🚀 Sample Workflow

### Scenario: Track a New Order

**Day 1 - Order Placed**
```
Status: WAITING_FOR_PROCESSING
Location: Guangzhou Factory
Description: Received customer order, starting production
```

**Day 5 - In Production**
```
Status: PROCESSING
Location: Guangzhou Factory
Description: Quality control passed, ready for packaging
```

**Day 8 - Ready to Ship**
```
Status: READY_TO_SHIP
Location: Guangzhou Warehouse
Description: Packaged and awaiting shipment
```

**Day 9 - In Transit**
```
Status: IN_TRANSIT
Location: Shanghai Distribution Hub
Description: Package is in transit to destination
```

**Day 12 - Out for Delivery**
```
Status: OUT_FOR_DELIVERY
Location: Local Delivery Center
Description: Package out for delivery today
```

**Day 12 - Delivered**
```
Status: DELIVERED
Location: Customer Address
Description: Successfully delivered
```

## 🛠️ Technical Details

### Database
- Table: `order_trackings`
- Foreign Key: `order_id` → `orders.order_id`
- Indexes on: `order_id`, `is_current` for fast queries

### Files Changed
- 📄 Entity: `OrderTracking.java`
- 📄 Repository: `OrderTrackingRepository.java`
- 📄 Controller: `TrackingController.java`
- 🎨 Template: `admin/order_tracking.html`
- 🔄 Fragment: `fragments/order_tracking.html`
- 💾 Migration: `V3__create_order_tracking_table.sql`

### Security
- ✅ Admin-only for management endpoints
- ✅ Role-based access control (ADMIN role required)
- ✅ Public read for current status (no auth needed)
- ✅ JWT authentication for admin endpoints

## ❓ FAQ

**Q: Can multiple admins update the same order?**  
A: Yes, each update creates a new tracking record with a timestamp

**Q: What happens to previous tracking steps?**  
A: They're retained in history, marked as not "current"

**Q: Can I edit a past tracking step?**  
A: No, new updates create new records. You can only add new statuses.

**Q: Does the customer see tracking in real-time?**  
A: Yes, the public API endpoint shows current status immediately

**Q: What if I make a mistake?**  
A: Just add a correction as a new tracking step with the correct info

## 🔗 Related Pages

- Admin Dashboard: `/admin/dashboard`
- Order Detail: `/orders/{orderId}`
- My Orders: `/orders`
- Chat: `/dashboard/chat`

---

**Version**: 1.0  
**Last Updated**: April 8, 2026  
**Status**: ✅ Production Ready
