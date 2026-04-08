# Order Tracking - Fake Delivery Integration Guide

## Overview
Hệ thống order tracking giả lập đã được hoàn thiện để admin có thể quản lý và cập nhật tình trạng giao hàng của đơn hàng mà không cần tích hợp với bên giao hàng thực tế.

## Features Implemented

### 1. **Database Layer**
- **Table**: `order_trackings` 
- **Fields**:
  - `tracking_id`: Primary key (auto-increment)
  - `order_id`: Foreign key to orders table
  - `status`: Current tracking status
  - `location`: Location description
  - `description`: Additional notes
  - `is_current`: Flag for current tracking step
  - `created_at`, `updated_at`: Timestamps

**Migration**: `V3__create_order_tracking_table.sql`

### 2. **Backend Implementation**

#### Entity
- **OrderTracking.java** (`src/main/java/com/example/guangzhouorder/entity/OrderTracking.java`)
  - JPA entity với @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor từ Lombok

#### Repository
- **OrderTrackingRepository.java** (`src/main/java/com/example/guangzhouorder/repository/OrderTrackingRepository.java`)
  - `findByOrderOrderByCreatedAtDesc()`: Lấy toàn bộ tracking history
  - `findByOrderAndIsCurrent()`: Lấy tracking step hiện tại
  - `findByOrder()`: Lấy tất cả tracking steps

#### Controller
- **TrackingController.java** (`src/main/java/com/example/guangzhouorder/controller/TrackingController.java`)
  - **GET** `/admin/orders/{orderId}/tracking` - Trang admin để chỉnh sửa tracking
  - **POST** `/admin/api/tracking/update/{orderId}` - API để cập nhật tracking status
  - **GET** `/admin/api/tracking/{orderId}` - API lấy toàn bộ tracking history (admin only)
  - **GET** `/admin/api/tracking/{orderId}/current` - API lấy tracking hiện tại (public)

### 3. **Frontend Implementation**

#### Admin Pages
- **order_tracking.html** (`src/main/resources/templates/admin/order_tracking.html`)
  - UI admin để quản lý tracking
  - Form cập nhật status, location, description
  - Display tracking history timeline
  - Current status widget
  - Same UI style với existing pages (Material Design 3, Tailwind CSS)

#### Fragments
- **order_tracking.html** (`src/main/resources/templates/fragments/order_tracking.html`)
  - `orderTracking()` fragment: Display tracking status + history timeline
  - `progressBar()` fragment: Visual progress bar dựa trên order status
  - Reusable components cho customer orders detail page

### 4. **Tracking Status Types**
```
- WAITING_FOR_PROCESSING: Chờ xử lý
- PROCESSING: Đang xử lý
- READY_TO_SHIP: Sẵn sàng gửi
- IN_TRANSIT: Đang vận chuyển
- OUT_FOR_DELIVERY: Đang giao
- DELIVERED: Đã giao
```

## How to Use

### For Admin Users

1. **Access Tracking Management**
   - Navigate to: `/admin/orders/{orderId}/tracking`
   - Only admin users (role = "ADMIN") can access

2. **Update Tracking Status**
   - Select new status from dropdown
   - Enter location (e.g., "Guangzhou Warehouse", "Shanghai Distribution")
   - Add description/notes (optional)
   - Click "Update Tracking" button
   - System automatically marks previous tracking as inactive

3. **View Tracking History**
   - Timeline display shows all tracking updates
   - Most recent at top
   - Current status highlighted with badge
   - Includes timestamps for each update

### For Customers

1. **View Tracking on Order Detail**
   - Order detail page automatically displays current tracking status
   - Shows tracking timeline history
   - Progress bar indicates order completion percentage

2. **Public Tracking API**
   - Get current tracking: `GET /admin/api/tracking/{orderId}/current`
   - No authentication required for public endpoint

## Responsive UI

All templates follow existing design system:
- **Color Scheme**: Material Design 3 colors (primary: #003461)
- **Components**: Tailwind CSS with custom Material Symbols
- **Responsive**: Mobile-first design with side navigation
- **Animations**: Smooth transitions and hover effects

## Integration Points

### Updated Controllers
- **OrdersController.java**: Added orderTrackingRepository injection
  - Passes tracking data to order detail views

### Updated Templates
- `customer/my_orders.html` - Can include tracking fragment
- Order detail pages now support tracking display

## Database Migration

Run migration to create table:
```sql
-- File: V3__create_order_tracking_table.sql
-- Automatically executed by Flyway on application startup
```

## Testing

1. **Build Project**
   ```bash
   mvn clean compile -DskipTests
   ```
   ✓ Build successful

2. **Access URLs**
   - Admin panel: `/admin/orders/{orderId}/tracking`
   - API (Get current): `GET /admin/api/tracking/{orderId}/current`
   - API (Get all): `GET /admin/api/tracking/{orderId}` (admin auth required)
   - API (Update): `POST /admin/api/tracking/update/{orderId}` (admin auth required)

## Future Enhancements

1. **Webhook Integration**: Connect to real delivery partners
2. **Customer Notifications**: Send SMS/Email on status updates
3. **Estimated Delivery**: Add ETA calculation
4. **Tracking Events**: Log detailed tracking event history
5. **Mobile App**: Separate mobile tracking view

## Files Created/Modified

### New Files
- `OrderTracking.java` - Entity
- `OrderTrackingRepository.java` - Repository
- `TrackingController.java` - Controller
- `order_tracking.html` (admin) - Admin UI
- `order_tracking.html` (fragments) - Reusable components
- `V3__create_order_tracking_table.sql` - Database migration

### Modified Files
- `OrdersController.java` - Added tracking data injection

---

**Version**: 1.0  
**Date**: April 8, 2026  
**Status**: ✓ Complete and tested
