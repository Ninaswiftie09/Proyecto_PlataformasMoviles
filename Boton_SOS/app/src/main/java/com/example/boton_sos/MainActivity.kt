package com.example.boton_sos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boton_sos.ui.theme.Boton_SOSTheme

// MainActivity: Configuración principal de la actividad, incluyendo la navegación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Boton_SOSTheme {
                val navController = rememberNavController()
                AppNavigator(navController)
            }
        }
    }
}

// AppNavigator: Configura el flujo de navegación entre pantallas
@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable("welcome_screen") { WelcomeScreen(navController) }
        composable("login_screen") { LoginScreen(navController) }
        composable("help_screen") { HelpScreen(navController) }
        composable("info_screen") { InfoScreen(navController) }
        composable("register_screen") { RegisterScreen(navController) }
        composable("emergency_numbers_screen") { EmergencyNumbersScreen(navController) }
        composable("hospitals_screen") { HospitalsScreen(navController) }
    }
}

// WelcomeScreen: Pantalla de bienvenida, que lleva a login
@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .clickable { navController.navigate("login_screen") },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.boton),
                contentDescription = "boton sos",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BOTÓN DE EMERGENCIA",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

// LoginScreen: Pantalla de login, con opciones de iniciar sesión o crear cuenta
@Composable
fun LoginScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.zona1),
            contentDescription = "Fondo de zona",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xAA000000))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Usuario") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Contraseña") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("help_screen") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar sesión", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¿Aún no tienes cuenta?",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xAA000000))
                    .padding(8.dp)
                    .clickable { navController.navigate("register_screen") }
            )
        }
    }
}

// HelpScreen: Pantalla del botón de emergencia, con un menú desplegable para navegar a otras pantallas
@Composable
fun HelpScreen(navController: NavHostController) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        IconButton(
            onClick = { menuExpanded = !menuExpanded },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = "❗",
                fontSize = 30.sp,
                color = Color.White
            )
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center),
            shape = CircleShape
        ) {
            Text(
                text = "HELP!",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(onClick = {
                navController.navigate("info_screen")
                menuExpanded = false
            }) {
                Text("Cuenta")
            }
            DropdownMenuItem(onClick = {
                navController.navigate("emergency_numbers_screen")
                menuExpanded = false
            }) {
                Text("Números de Emergencia")
            }
            DropdownMenuItem(onClick = {
                navController.navigate("hospitals_screen")
                menuExpanded = false
            }) {
                Text("Hospitales Cercanos")
            }
        }
    }
}

// RegisterScreen: Pantalla para crear una cuenta
@Composable
fun RegisterScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var emergencyContactName1 by remember { mutableStateOf("") }
    var emergencyContactPhone1 by remember { mutableStateOf("") }
    var emergencyContactName2 by remember { mutableStateOf("") }
    var emergencyContactPhone2 by remember { mutableStateOf("") }

    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Crear Cuenta", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = if (bloodType.isEmpty()) "Tipo de Sangre" else bloodType,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = if (bloodType.isEmpty()) Color.Gray else Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bloodTypes.forEach { type ->
                DropdownMenuItem(onClick = {
                    bloodType = type
                    expanded = false
                }) {
                    Text(text = type)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emergencyContactName1,
            onValueChange = { emergencyContactName1 = it },
            label = { Text("Contacto de Emergencia 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emergencyContactPhone1,
            onValueChange = { emergencyContactPhone1 = it },
            label = { Text("Teléfono 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emergencyContactName2,
            onValueChange = { emergencyContactName2 = it },
            label = { Text("Contacto de Emergencia 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emergencyContactPhone2,
            onValueChange = { emergencyContactPhone2 = it },
            label = { Text("Teléfono 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar")
        }
    }
}

// InfoScreen: Pantalla de información de la cuenta
@Composable
fun InfoScreen(navController: NavHostController) {
    // Lógica para la pantalla de información
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Información", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Aquí va la información del usuario.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("help_screen") }) {
            Text(text = "Regresar")
        }
    }
}

// EmergencyNumbersScreen: Pantalla de números de emergencia
@Composable
fun EmergencyNumbersScreen(navController: NavHostController) {
    val emergencyNumbers = listOf(
        "Policía: 110",
        "Bomberos: 123",
        "Ambulancia: 125",
        "Emergencias Médicas: 120"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Números de Emergencia",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        emergencyNumbers.forEach { number ->
            Text(
                text = number,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() }, 
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Regresar", color = Color.White)
        }
    }
}


// HospitalsScreen: Pantalla de hospitales cercanos
@Composable
fun HospitalsScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa",
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Hospitales Cercanos",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = {},
                label = { Text("Buscar Hospital") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
        }
    }
}


@Composable
fun Background() {
    Image(
        painter = painterResource(id = R.drawable.zona1),
        contentDescription = "Fondo de la aplicación",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}