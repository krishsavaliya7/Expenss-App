package com.splitsmart.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.splitsmart.app.ui.screens.auth.*
import com.splitsmart.app.ui.screens.dashboard.*
import com.splitsmart.app.ui.screens.friends.*
import com.splitsmart.app.ui.screens.groups.*
import com.splitsmart.app.ui.screens.ledger.*
import com.splitsmart.app.ui.screens.notifications.*
import com.splitsmart.app.ui.screens.profile.*

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val GROUPS = "groups"
    const val GROUP_DETAIL = "group/{groupId}"
    const val CREATE_GROUP = "groups/create"
    const val ADD_EXPENSE = "group/{groupId}/add-expense"
    const val SETTLEMENT = "group/{groupId}/settlement"
    const val FRIENDS = "friends"
    const val LEDGER = "ledger"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
}

@Composable
fun SplitSmartNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            val vm: AuthViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            androidx.compose.runtime.LaunchedEffect(state.user) {
                if (state.user != null) navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } }
            }
            LoginScreen(state, onLogin = { l, p -> vm.login(l, p) }, onNavigateToSignup = { navController.navigate(Routes.SIGNUP) }, onClearError = { vm.clearError() })
        }

        composable(Routes.SIGNUP) {
            val vm: AuthViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            androidx.compose.runtime.LaunchedEffect(state.user) {
                if (state.user != null) navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } }
            }
            SignupScreen(state, onSignup = { e, u, f, ph, upi, p -> vm.signup(e, u, f, ph, upi, p) }, onNavigateToLogin = { navController.popBackStack() }, onClearError = { vm.clearError() })
        }

        composable(Routes.DASHBOARD) {
            val vm: DashboardViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            DashboardScreen(state, onGroupClick = { navController.navigate("group/$it") }, onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) }, onAddExpense = { navController.navigate(Routes.CREATE_GROUP) }, onRefresh = { vm.loadDashboard() })
        }

        composable(Routes.GROUPS) {
            val vm: GroupViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            GroupsListScreen(state, onGroupClick = { navController.navigate("group/$it") }, onCreateGroup = { navController.navigate(Routes.CREATE_GROUP) }, onRefresh = { vm.loadGroups() })
        }

        composable(Routes.GROUP_DETAIL, arguments = listOf(navArgument("groupId") { type = NavType.IntType })) { entry ->
            val groupId = entry.arguments?.getInt("groupId") ?: return@composable
            val vm: GroupViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            LaunchedEffectLoadGroup(vm, groupId)
            GroupDetailScreen(state, groupId, onAddExpense = { navController.navigate("group/$groupId/add-expense") }, onSettlement = { navController.navigate("group/$groupId/settlement") }, onBack = { navController.popBackStack() }, onDeleteExpense = { vm.deleteExpense(groupId, it) }, onRefresh = { vm.loadGroupDetail(groupId) })
        }

        composable(Routes.CREATE_GROUP) {
            val vm: GroupViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            CreateGroupScreen(state, onCreateGroup = { n, d, c, m -> vm.createGroup(n, d, c, m) }, onBack = { navController.popBackStack() }, onGroupCreated = { vm.clearCreatedGroupId(); navController.navigate("group/$it") { popUpTo(Routes.CREATE_GROUP) { inclusive = true } } })
        }

        composable(Routes.ADD_EXPENSE, arguments = listOf(navArgument("groupId") { type = NavType.IntType })) { entry ->
            val groupId = entry.arguments?.getInt("groupId") ?: return@composable
            val vm: GroupViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            LaunchedEffectLoadGroup(vm, groupId)
            AddExpenseScreen(state, groupId, onCreateExpense = { vm.createExpense(groupId, it) }, onBack = { navController.popBackStack() }, onExpenseCreated = { vm.clearExpenseCreated(); navController.popBackStack() })
        }

        composable(Routes.SETTLEMENT, arguments = listOf(navArgument("groupId") { type = NavType.IntType })) { entry ->
            val groupId = entry.arguments?.getInt("groupId") ?: return@composable
            val vm: GroupViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            LaunchedEffectLoadSettlements(vm, groupId)
            SettlementScreen(state, groupId, onRequestCash = { to, amt -> vm.requestCashSettlement(groupId, to, amt) }, onInitiateUpi = { to, amt -> vm.initiateUpiSettlement(groupId, to, amt) }, onBack = { navController.popBackStack() })
        }

        composable(Routes.FRIENDS) {
            val vm: FriendsViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            FriendsScreen(state, onSearch = { vm.searchUsers(it) }, onSendRequest = { vm.sendFriendRequest(it) }, onAcceptRequest = { vm.acceptFriendRequest(it) }, onRejectRequest = { vm.rejectFriendRequest(it) }, onRefresh = { vm.loadFriends() })
        }

        composable(Routes.LEDGER) {
            val vm: LedgerViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            LedgerScreen(state, onSelectGroup = { vm.selectGroup(it) }, onRefresh = { vm.loadGroups() })
        }

        composable(Routes.PROFILE) {
            val vm: ProfileViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            ProfileScreen(state, onToggleEdit = { vm.toggleEdit() }, onSave = { f, e, p, u -> vm.updateProfile(f, e, p, u) }, onLogout = { vm.logout { navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } } } })
        }

        composable(Routes.NOTIFICATIONS) {
            val vm: NotificationsViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            NotificationsScreen(state, onMarkAllRead = { vm.markAllRead() }, onBack = { navController.popBackStack() }, onRefresh = { vm.loadNotifications() })
        }
    }
}

@Composable
private fun LaunchedEffectLoadGroup(vm: GroupViewModel, groupId: Int) {
    androidx.compose.runtime.LaunchedEffect(groupId) { vm.loadGroupDetail(groupId) }
}

@Composable
private fun LaunchedEffectLoadSettlements(vm: GroupViewModel, groupId: Int) {
    androidx.compose.runtime.LaunchedEffect(groupId) { vm.loadSettlements(groupId) }
}
