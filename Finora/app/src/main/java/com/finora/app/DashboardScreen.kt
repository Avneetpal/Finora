package com.finora.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

// ---- Development data (replaced by Room later) ----
data class Txn(val title: String, val category: String, val amount: Double, val date: String, val color: Color)
data class CatSpend(val name: String, val spent: Double, val budget: Double, val color: Color)

private val txns = listOf(
    Txn("Salary – Acme Pvt Ltd", "Income", 85000.0, "01/10/2026", Mint),
    Txn("Swiggy", "Food & Dining", -640.0, "29/10/2026", Coral),
    Txn("Big Bazaar", "Groceries", -2150.0, "28/10/2026", Amber),
    Txn("Netflix", "Subscriptions", -649.0, "27/10/2026", Violet),
    Txn("Petrol", "Fuel", -1800.0, "26/10/2026", Blue),
)
private val cats = listOf(
    CatSpend("Food & Dining", 8200.0, 10000.0, Coral),
    CatSpend("Groceries", 6400.0, 8000.0, Amber),
    CatSpend("Transport", 3900.0, 6000.0, Blue),
    CatSpend("Shopping", 7600.0, 8000.0, Violet),
)

fun money(v: Double): String = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    .apply { maximumFractionDigits = 0 }.format(v)

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier.fillMaxWidth().clip(shape)
            .background(Brush.linearGradient(listOf(Color.White.copy(0.12f), Color.White.copy(0.04f))))
            .border(1.dp, Color.White.copy(0.14f), shape).padding(16.dp),
        content = content
    )
}

@Composable
fun AnimatedMoney(value: Double, size: Int, color: Color = Color.White) {
    val v by animateFloatAsState(value.toFloat(), tween(1000), label = "counter")
    Text(money(v.toDouble()), color = color, fontSize = size.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun DashboardScreen() {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf<Pair<String, ImageVector>>(
        "Home" to Icons.Filled.Home, "Transactions" to Icons.AutoMirrored.Filled.List,
        "Budgets" to Icons.Filled.DateRange, "Analytics" to Icons.Filled.Star, "More" to Icons.Filled.MoreVert)
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF111A3D), Navy, Color(0xFF1A0F33))))) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(onClick = { /* TODO: add transaction sheet */ },
                    containerColor = Blue, contentColor = Color.White, shape = CircleShape) {
                    Icon(Icons.Filled.Add, contentDescription = "Add transaction")
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White.copy(0.06f)) {
                    tabs.forEachIndexed { i, (label, icon) ->
                        NavigationBarItem(selected = tab == i, onClick = { tab = i },
                            icon = { Icon(icon, label) }, label = { Text(label, fontSize = 10.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White, indicatorColor = Blue.copy(0.35f),
                                unselectedIconColor = Color.White.copy(0.6f),
                                selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(0.6f)))
                    }
                }
            }
        ) { pad ->
            LazyColumn(
                Modifier.statusBarsPadding(),
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, pad.calculateBottomPadding() + 88.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text("Good evening", color = Color.White.copy(0.65f), fontSize = 14.sp)
                    Text("Finora", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                item {
                    GlassCard {
                        Text("Total balance", color = Color.White.copy(0.65f))
                        AnimatedMoney(248500.0, 36)
                        Spacer(Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Stat("Income", 85000.0, Mint, Modifier.weight(1f))
                            Stat("Expenses", 26700.0, Coral, Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Saved ₹58,300 • 68.6% of income", color = Mint, fontSize = 13.sp)
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlassCard(Modifier.weight(1f)) {
                            Text("Net worth", color = Color.White.copy(0.65f), fontSize = 12.sp)
                            AnimatedMoney(612000.0, 18)
                        }
                        GlassCard(Modifier.weight(1f)) {
                            Text("Debt", color = Color.White.copy(0.65f), fontSize = 12.sp)
                            AnimatedMoney(185000.0, 18, Coral)
                        }
                    }
                }
                item {
                    GlassCard {
                        Text("Budget progress", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        cats.forEach { c ->
                            val p = (c.spent / c.budget).toFloat()
                            val anim by animateFloatAsState(p, tween(900), label = "bar")
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(c.name, fontSize = 13.sp)
                                Text("${money(c.spent)} / ${money(c.budget)}", fontSize = 12.sp, color = Color.White.copy(0.65f))
                            }
                            Spacer(Modifier.height(6.dp))
                            Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Color.White.copy(0.1f))) {
                                Box(Modifier.fillMaxWidth(anim.coerceIn(0f, 1f)).fillMaxHeight().clip(CircleShape)
                                    .background(if (p >= 0.9f) Coral else c.color))
                            }
                        }
                    }
                }
                item {
                    GlassCard {
                        Text("Recent transactions", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        txns.forEach { t ->
                            Row(Modifier.fillMaxWidth().padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(40.dp).clip(CircleShape).background(t.color.copy(0.2f)), contentAlignment = Alignment.Center) {
                                    Text(t.title.first().toString(), color = t.color, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(t.title, fontSize = 14.sp, maxLines = 1)
                                    Text("${t.category} • ${t.date}", fontSize = 11.sp, color = Color.White.copy(0.55f))
                                }
                                Text((if (t.amount > 0) "+" else "-") + money(kotlin.math.abs(t.amount)),
                                    color = if (t.amount > 0) Mint else Coral, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                item {
                    Text("Sample data for development only. Projections and insights will be labelled as estimates.",
                        color = Color.White.copy(0.4f), fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: Double, color: Color, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(16.dp)).background(color.copy(0.12f)).padding(12.dp)) {
        Text(label, fontSize = 12.sp, color = color)
        AnimatedMoney(value, 18)
    }
}
