# 🚀 Quick Reference Card - Order Tracking System

## 📍 Access Points

| Purpose | URL | Access |
|---------|-----|--------|
| **Admin Dashboard** | `/admin/dashboard` | ADMIN only |
| **Edit Tracking** | `/admin/orders/{id}/tracking` | ADMIN only |
| **View Order** | `/orders/{id}` | Customer/Admin |
| **Get Current Tracking** | `GET /admin/api/tracking/{id}/current` | Public API |
| **Update Tracking** | `POST /admin/api/tracking/update/{id}` | ADMIN only |

## 🎯 Quick Start (5 Minutes)

### Step 1: Admin logs in
```
1. Go to https://your-domain.com/admin/dashboard
2. See overview of all orders
```

### Step 2: Find order to update
```
1. Use search box to find order
   - Search by: Order ID, Customer name, Status, Payment
2. OR scroll through table manually
```

### Step 3: Update tracking
```
1. Hover over order row
2. Click "Edit Tracking" button (📍 icon)
3. Select status: WAITING_FOR_PROCESSING, PROCESSING, READY_TO_SHIP, 
                  IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED
4. Enter location: e.g., "Shanghai Hub", "Beijing Center"
5. Add note: Optional description
6. Click "Update Tracking"
7. Done! Customer sees update immediately
```

## 📊 Status Codes

| Code | Meaning | Color | Icon |
|------|---------|-------|------|
| WAITING_FOR_PROCESSING | Awaiting action | Gray | ⏳ |
| PROCESSING | Being worked on | Blue | 🔄 |
| READY_TO_SHIP | Ready to go | Orange | 📦 |
| IN_TRANSIT | On the way | Blue | 🚚 |
| OUT_FOR_DELIVERY | Last mile | Green | 🚚 |
| DELIVERED | Completed | Green | ✅ |

## 💾 Database Table Structure

```
order_trackings table:
├── tracking_id (Primary Key, Auto-increment)
├── order_id (Foreign Key → orders)
├── status (VARCHAR 50)
├── location (VARCHAR 500)
├── description (TEXT)
├── is_current (BOOLEAN)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)

Indexes:
├── order_id
├── is_current
└── order_id + created_at
```

## 🔌 API Examples

### Get Current Tracking Status
```bash
curl GET "https://api.example.com/admin/api/tracking/123/current"

Response:
{
  "trackingId": 5,
  "orderId": 123,
  "status": "IN_TRANSIT",
  "location": "Shanghai Distribution",
  "description": "Package on the way",
  "isCurrent": true,
  "createdAt": "2026-04-08T14:30:00"
}
```

### Update Tracking
```bash
curl -X POST "https://api.example.com/admin/api/tracking/update/123" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_TRANSIT",
    "location": "Shanghai Hub",
    "description": "Package in transit"
  }'

Response:
{
  "success": true,
  "message": "Tracking updated successfully"
}
```

### Get All Tracking History
```bash
curl GET "https://api.example.com/admin/api/tracking/123" \
  -H "Authorization: Bearer {JWT_TOKEN}"

Response:
[
  {
    "trackingId": 5,
    "status": "IN_TRANSIT",
    "location": "Shanghai",
    "isCurrent": true,
    "createdAt": "2026-04-08T14:30:00"
  },
  {
    "trackingId": 4,
    "status": "READY_TO_SHIP",
    "location": "Guangzhou Warehouse",
    "isCurrent": false,
    "createdAt": "2026-04-07T10:00:00"
  }
]
```

## 📋 Dashboard Statistics Explained

| Stat | What It Shows | Where to Find |
|------|---------------|----------------|
| **Total Orders** | All orders ever created | Top left card |
| **In Production** | Currently being made | Top 2nd card |
| **Pending Approval** | Awaiting visual proof | Top 3rd card |
| **Completed** | Finished & paid | Top right card |
| **Negotiating** | Price being negotiated | Bottom left panel |
| **Unpaid** | No payment received | Bottom middle panel |
| **Deposit Paid** | 30% paid, awaiting balance | Bottom middle panel |

## 🔐 Security Notes

- ✅ Admin role required for dashboard access
- ✅ JWT token needed for API endpoints
- ✅ Non-admin users see redirect
- ✅ All changes timestamped
- ✅ Read-only public tracking API

## 📱 Device Support

| Device | Layout | Features |
|--------|--------|----------|
| Desktop (1200px+) | Full 2-3 column | All visible |
| Tablet (768-1199px) | 1-2 columns | Touch optimized |
| Mobile (< 768px) | Single column | Scroll table |

## 🎨 Color Palette

```
Primary: #003461 (Dark Blue)
Primary Light: #004b87 (Medium Blue)
Primary Lighter: #d3e4ff (Light Blue)

Status Colors:
- In Production: Blue (#2563eb)
- Pending: Yellow (#fbbf24)
- Completed: Green (#10b981)
- Error: Red (#ef4444)

Background: #f8f9fb (Very Light Gray)
Text: #191c1e (Dark Gray)
Muted: #424750 (Medium Gray)
```

## 📂 File Locations

```
Backend:
├── src/main/java/.../controller/
│   ├── TrackingController.java
│   └── AdminDashboardController.java
├── src/main/java/.../entity/
│   └── OrderTracking.java
└── src/main/java/.../repository/
    └── OrderTrackingRepository.java

Frontend:
├── src/main/resources/templates/admin/
│   ├── dashboard.html
│   └── order_tracking.html
└── src/main/resources/templates/fragments/
    └── order_tracking.html

Database:
└── src/main/resources/db/migration/
    └── V3__create_order_tracking_table.sql
```

## ⚡ Performance Tips

1. **Search Large Tables**: Use order ID or customer email for faster results
2. **Batch Updates**: Update multiple orders systematically
3. **Cache**: Browser caches dashboard stats (refresh if needed)
4. **Mobile**: Use simplified mobile view for faster loading

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Can't see dashboard | Check if logged in as admin |
| Search not working | Wait for page load, try refresh |
| Tracking not updating | Check browser console for errors |
| Buttons not visible | Hover over order row |
| Mobile layout broken | Update browser, clear cache |

## 🔗 Related Resources

- **Full Docs**: TRACKING_IMPLEMENTATION.md
- **User Guide**: ORDER_TRACKING_GUIDE.md
- **Dashboard Guide**: ADMIN_DASHBOARD_GUIDE.md
- **API Docs**: Built-in API documentation

## 📞 Keyboard Shortcuts

```
Ctrl/Cmd + F → Search on page
F5 → Refresh dashboard
Esc → Close modals
Tab → Navigate between form fields
Enter → Submit forms
```

## 📊 Example Tracking Journey

```
Customer places order
    ↓
Status: WAITING_FOR_PROCESSING (Guangzhou Factory)
    ↓
Status: PROCESSING (Quality checks)
    ↓
Status: READY_TO_SHIP (Packaged)
    ↓
Status: IN_TRANSIT (Shanghai Hub)
    ↓
Status: OUT_FOR_DELIVERY (Local center)
    ↓
Status: DELIVERED (Customer received)
```

## 💡 Pro Tips

1. **Use specific locations**: "Shanghai Hub" instead of just "Shanghai"
2. **Add helpful descriptions**: Customers appreciate detailed updates
3. **Update regularly**: Keep customers informed
4. **Check dashboard daily**: Identify bottlenecks early
5. **Use search effectively**: Filter by status to find bulk updates

---

**Version**: 1.0  
**Last Updated**: April 8, 2026  
**Quick Ref**: Print this card for office wall! 📌
