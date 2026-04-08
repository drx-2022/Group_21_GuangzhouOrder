# Admin Dashboard - Complete Guide

## 🎯 Overview

Trang **Admin Dashboard** (`/admin/dashboard`) là trung tâm quản lý toàn bộ đơn hàng và tracking. Từ đây admin có thể:
- Xem overview tất cả orders
- Chỉ mục nhanh status các đơn
- Cập nhật tracking trực tiếp
- Tìm kiếm/filter orders

## 🚀 Accessing Admin Dashboard

### URL
```
https://your-domain.com/admin/dashboard
```

### Requirements
- Must be logged in
- Must have **ADMIN** role
- Non-admin users will be redirected to home

## 📊 Dashboard Sections

### 1. **Statistics Cards** (Top Section)

4 main metric cards showing:

| Card | Shows | Color | Icon |
|------|-------|-------|------|
| **Total Orders** | All orders in system | Blue | 📦 |
| **In Production** | Currently being made | Blue | 🏭 |
| **Pending Approval** | Awaiting visual proof | Yellow | ⏳ |
| **Completed** | Finished orders | Green | ✅ |

Example values:
```
Total Orders: 150
In Production: 23
Pending Approval: 8
Completed: 112
```

### 2. **Quick Stats Panels** (Below Statistics)

Three columns showing additional breakdown:

**Column 1: Order Status Distribution**
```
- Negotiating: 5
- Ready for Shipping: 12
- Draft: 3
- Cancelled: 2
```

**Column 2: Payment Status**
```
- Unpaid: 8
- Deposit Paid: 15
- Fully Paid: 127
```

**Column 3: Quick Actions**
```
- View Store (link)
- Export Report (button)
```

### 3. **Orders Table** (Main Section)

Complete list of all orders with columns:

| Column | Content | Example |
|--------|---------|---------|
| **Order ID** | Unique order identifier | GZ-ORD-123 |
| **Customer** | Name + email | John Doe (john@email.com) |
| **Status** | Production status | IN_PRODUCTION |
| **Tracking** | Current tracking info | IN_TRANSIT, Shanghai |
| **Payment** | Payment status | DEPOSITED |
| **Created** | Order creation date | 15 Jan 2024 |
| **Actions** | Quick edit/view buttons | [Tracking] [View] |

## 🔍 How to Use the Dashboard

### Step 1: View Orders Overview
1. Navigate to `/admin/dashboard`
2. See all statistics at top
3. Scan orders table for status overview

### Step 2: Search for Specific Order
1. Click search box at top of table
2. Type any text (Order ID, customer name, status, etc.)
3. Table automatically filters in real-time
4. Results update as you type

Example searches:
```
- "GZ-ORD-123" → Find specific order
- "John Doe" → Find customer's orders
- "IN_PRODUCTION" → Find all orders in production
- "UNPAID" → Find unpaid orders
```

### Step 3: Edit Tracking Status
1. Find order in table
2. Hover over the row (action buttons appear)
3. Click **"📍 Edit Tracking"** button
4. Opens `/admin/orders/{orderId}/tracking` page
5. Update:
   - Status (dropdown: WAITING_FOR_PROCESSING, PROCESSING, READY_TO_SHIP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED)
   - Location (e.g., "Shanghai Hub", "Beijing Center")
   - Description (e.g., "Package in transit")
6. Click **"Update Tracking"** to save

### Step 4: View Order Details
1. Find order in table
2. Hover over the row (action buttons appear)
3. Click **"👁️ View Order"** button
4. Opens full order detail page
5. See all order info, customer details, tracking history

## 📱 Responsive Design

### Desktop (1200px+)
- Full table with all columns visible
- Side-by-side statistics
- Hover effects on buttons

### Tablet (768px - 1199px)
- Table columns optimized for space
- Statistics in 2 columns
- Touch-friendly buttons

### Mobile (< 768px)
- Table scrolls horizontally
- Statistics stacked vertically
- Simplified display
- Easy-to-tap action buttons

## 🎨 Color Coding

### Order Status Colors
```
- IN_PRODUCTION → Blue
- DONE → Green
- PENDING_CUSTOMER_APPROVAL → Yellow
- NEGOTIATING → Purple
- Others → Gray
```

### Payment Status Colors
```
- UNPAID → Red
- DEPOSITED → Yellow
- DONE (Fully Paid) → Green
```

## ✨ Key Features

### Real-Time Search
- Type anything to filter
- Searches: ID, customer name, status, payment
- Case-insensitive
- Results update instantly

### Hover Actions
- Action buttons appear on hover
- Two options per row:
  1. **Edit Tracking** - Quick access to tracking page
  2. **View Order** - Full order details

### Quick Stats
- See distribution at glance
- Helps identify bottlenecks
- Track business metrics

### Sorting
- Click column headers to sort (if implemented)
- Orders sorted by date by default

## 📈 Common Workflows

### Workflow 1: Daily Check-in
1. Open `/admin/dashboard`
2. Scan statistics for overview
3. Look for "Pending Approval" orders
4. Prioritize which orders to update

### Workflow 2: Update All Shipments
1. Search for "IN_PRODUCTION" status
2. For each, click Edit Tracking
3. Update to "IN_TRANSIT" with location
4. Return to dashboard, refresh

### Workflow 3: Find Unpaid Orders
1. Search for "UNPAID" in Payment column
2. Contact those customers
3. Send payment reminders

### Workflow 4: Process New Orders
1. Search for "DRAFT" status
2. Open each order
3. Update tracking as production progresses

## 🔒 Security

- ✅ Admin-only access enforced
- ✅ Role-based authentication
- ✅ No sensitive data exposed
- ✅ Safe CRUD operations
- ✅ All changes logged

## 🐛 Troubleshooting

### Q: Can't access dashboard?
A: Check if you're logged in as admin. Non-admin users will see redirect to home.

### Q: Search not working?
A: Wait a moment for page to load completely. Try refreshing if stuck.

### Q: Tracking button not appearing?
A: Hover over the order row. Action buttons only show on hover.

### Q: Orders list empty?
A: Create orders from `/products` or check if you're viewing correct business unit.

### Q: Statistics not updating?
A: Refresh page with F5 or Ctrl+Shift+R to clear cache.

## 🔗 Related Pages

- **Tracking Edit**: `/admin/orders/{orderId}/tracking`
- **Order Detail**: `/orders/{orderId}`
- **Admin Chat**: `/admin/chat`
- **Home**: `/`

## 📊 API Integration

Dashboard fetches:
- All orders from `OrderRepository.findAll()`
- Current tracking from `OrderTrackingRepository.findByOrderAndIsCurrent(order, true)`
- Aggregated statistics in real-time

## 🎯 Best Practices

1. **Regular Updates**: Update tracking daily
2. **Accurate Locations**: Use specific location names
3. **Detailed Descriptions**: Help customers understand status
4. **Timely Actions**: Don't let orders stall
5. **Regular Checks**: Review dashboard each morning

## 📱 Mobile Optimization

- Touch-friendly buttons (48px minimum)
- Readable text on small screens
- Horizontal scrolling for wide table
- Easy search on mobile
- Quick access to tracking editor

---

**Version**: 1.0  
**Last Updated**: April 8, 2026  
**Status**: ✅ Production Ready
