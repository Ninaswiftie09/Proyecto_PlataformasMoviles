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

@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable("welcome_screen") { WelcomeScreen(navController) }
        composable("login_screen") { LoginScreen(navController) }
        composable("help_screen") { HelpScreen(navController) }
        composable("info_screen") { InfoScreen(navController) }
        composable("register_screen"){ RegisterScreen(navController)}
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .clickable { navController.navigate("login_screen") }, // Navegar a "login_screen"
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

@Composable
fun HelpScreen(navController: NavHostController) {
    // Estado para controlar el menú desplegable
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
            // Emoji para el índice (¡No a las imagenes!)
            Text(
                text = "❗",
                fontSize = 30.sp,
                color = Color.White
            )
        }

        // Botón circular "HELP!" centrado (no funcional por el momento)
        Button(
            onClick = { /* No hacer nada al presionar */ },
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
            DropdownMenuItem(onClick = {}) {
                Text("Cuenta")
            }
            DropdownMenuItem(onClick = {}) {
                Text("Números de Emergencia")
            }
            DropdownMenuItem(onClick = {}) {
                Text("Hospitales Cercanos")
            }
        }
    }
}


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

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = bloodType,
                onValueChange = { }, // No permite editar directamente
                label = { Text("Tipo de Sangre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true // Hace que el campo sea solo lectura
            )
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
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emergencyContactName1,
            onValueChange = { emergencyContactName1 = it },
            label = { Text("Nombre de Emergencia 1   "         ) },
                    modifier = Modifier.fillMaxWidth()
            )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = emergencyContactPhone1,
                    onValueChange = { emergencyContactPhone1 = it },
                    label = { Text("Teléfono de Emergencia 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = emergencyContactName2,
                    onValueChange = { emergencyContactName2 = it },
                    label = { Text("Nombre de Emergencia 2") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = emergencyContactPhone2,
                    onValueChange = { emergencyContactPhone2 = it },
                    label = { Text("Teléfono de Emergencia 2") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Lógica para registrar la cuenta (por el momento, solo regresar a la página de inicio)
                        navController.navigate("welcome_screen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrar")
                }
            }
    }

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
            Text(text = "Aquí puedes proporcionar información sobre tu aplicación.", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("welcome_screen") }) {
                Text(text = "Regresar")
            }
        }
    }

