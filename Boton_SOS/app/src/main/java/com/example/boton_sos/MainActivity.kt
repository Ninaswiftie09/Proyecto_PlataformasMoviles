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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.json.JSONObject
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel


// MainActivity: Configuración principal de la actividad, incluyendo la navegación
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

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
fun LoginScreen(navController: NavHostController, viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authResult by viewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        authResult?.let {
            if (it.isSuccess) {
                navController.navigate("help_screen")
                viewModel.clearAuthResult()
            }
        }
    }

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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.login(email, password) },
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

        authResult?.exceptionOrNull()?.message?.let { errorMessage ->
            Text("Error al iniciar sesión: $errorMessage", color = Color.Red)
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
                color = Color.Red
            )
        }

        Button(
            onClick = { navController.navigate("emergency_numbers_screen") },
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
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            DropdownMenuItem(onClick = {
                navController.navigate("info_screen")
                menuExpanded = false
            }) {
                Text("Cuenta", color = Color.White)
            }
            DropdownMenuItem(onClick = {
                navController.navigate("emergency_numbers_screen")
                menuExpanded = false
            }) {
                Text("Números de Emergencia", color = Color.White)
            }
            DropdownMenuItem(onClick = {
                navController.navigate("hospitals_screen")
                menuExpanded = false
            }) {
                Text("Hospitales Cercanos", color = Color.White)
            }
        }
    }
}

// RegisterScreen: Pantalla para crear una cuenta
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: AuthViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E1E),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
        ) {
            Text(
                text = if (bloodType.isEmpty()) "Tipo de Sangre" else bloodType,
                color = if (bloodType.isEmpty()) Color.Gray else Color.Black
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            bloodTypes.forEach { type ->
                DropdownMenuItem(onClick = {
                    bloodType = type
                    expanded = false
                }) {
                    Text(text = type)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactName1,
            onValueChange = { emergencyContactName1 = it },
            label = { Text("Contacto de Emergencia 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactPhone1,
            onValueChange = { emergencyContactPhone1 = it },
            label = { Text("Teléfono 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactName2,
            onValueChange = { emergencyContactName2 = it },
            label = { Text("Contacto de Emergencia 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactPhone2,
            onValueChange = { emergencyContactPhone2 = it },
            label = { Text("Teléfono 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar", color = Color.White)
        }

        val authResult by viewModel.authResult.collectAsState()
        authResult?.let {
            if (it.isSuccess) {
                navController.navigate("login_screen")
            } else {
                Text("Error al registrar: ${it.exceptionOrNull()?.message}")
            }
        }
    }
}


// InfoScreen: Pantalla de información de la cuenta
@Composable
fun InfoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB71C1C))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Información",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Aquí va la información del usuario.",
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("help_screen") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Regresar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
            .background(Color(0xFFB71C1C))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Números de Emergencia",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
                .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            emergencyNumbers.forEach { number ->
                Text(
                    text = number,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Regresar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
private lateinit var fusedLocationClient: FusedLocationProviderClient

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
    } else {
        getUserLocation()
    }
}

private fun getUserLocation() {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            // Pasa la ubicación a la función de pantalla de hospitales
            loadHospitalsScreen(location.latitude, location.longitude)
        }
    }
}

private fun loadHospitalsScreen(latitude: Double, longitude: Double) {
    setContent {
        HospitalsScreen(navController = rememberNavController(), latitude, longitude)
    }
}

@Composable
fun HospitalsScreen(navController: NavHostController, latitude: Double, longitude: Double) {
    var hospitalsList by remember { mutableStateOf(listOf<String>()) }
    var connectionError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val hospitalsApiClient = HospitalsApiClient(context)

    // Llama a la API al iniciar la pantalla
    LaunchedEffect(Unit) {
        hospitalsApiClient.fetchNearbyHospitals(lat = latitude, lon = longitude) { result ->
            if (result != null) {
                hospitalsList = result
                connectionError = false
            } else {
                connectionError = true
                hospitalsList = listOf()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB71C1C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Hospitales Cercanos",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (connectionError) {
                Text(
                    text = "No hay conexión a internet",
                    color = Color.Yellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            hospitalsList.forEach { hospital ->
                Text(
                    text = hospital,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(4.dp))
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}


fun processHospitalsResponse(jsonResponse: String, query: String): List<String> {
    val hospitals = mutableListOf<String>()
    val jsonArray = JSONObject(jsonResponse).getJSONArray("elements")
    for (i in 0 until jsonArray.length()) {
        val element = jsonArray.getJSONObject(i)
        val tags = element.getJSONObject("tags")
        val name = tags.optString("name", "Hospital sin nombre")


        if (name.contains(query, ignoreCase = true)) {
            hospitals.add(name)
        }
    }
    return hospitals
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
