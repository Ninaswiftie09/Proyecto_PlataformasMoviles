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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import android.content.pm.PackageManager.PERMISSION_GRANTED




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
            val viewModel: AuthViewModel = viewModel()
            Boton_SOSTheme {
                val navController = rememberNavController()
                AppNavigator(navController, viewModel)
            }
        }
    }
}

// AppNavigator: Configura el flujo de navegación entre pantallas
@Composable
fun AppNavigator(navController: NavHostController, viewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable("welcome_screen") { WelcomeScreen(navController) }
        composable("login_screen") { LoginScreen(navController, viewModel) }
        composable("help_screen") { HelpScreen(navController) }
        composable("info_screen") { InfoScreen(navController, viewModel) }
        composable("register_screen") { RegisterScreen(navController, viewModel) }
        composable("emergency_numbers_screen") { EmergencyNumbersScreen(navController, viewModel) }
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
                val uid = it.getOrNull()?.user?.uid
                if (uid != null) {
                    viewModel.fetchUserData(uid)
                    // Esperar hasta que userInfo no sea null
                    viewModel.userInfo.collect { userInfo ->
                        if (userInfo != null) {
                            navController.navigate("help_screen")
                        }
                    }
                }
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
    var errorMessage by remember { mutableStateOf("") }
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

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = if (bloodType.isEmpty()) "Tipo de Sangre" else bloodType,
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

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactName1,
            onValueChange = { emergencyContactName1 = it },
            label = { Text("Contacto de Emergencia 1") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactPhone1,
            onValueChange = { emergencyContactPhone1 = it },
            label = { Text("Teléfono 1") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactName2,
            onValueChange = { emergencyContactName2 = it },
            label = { Text("Contacto de Emergencia 2") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = emergencyContactPhone2,
            onValueChange = { emergencyContactPhone2 = it },
            label = { Text("Teléfono 2") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE57373),
                unfocusedIndicatorColor = Color(0xFFBDBDBD)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || bloodType.isEmpty() ||
                    emergencyContactName1.isEmpty() || emergencyContactPhone1.isEmpty() || emergencyContactName2.isEmpty() || emergencyContactPhone2.isEmpty()) {
                    errorMessage = "Por favor, llena todos los campos"
                } else {
                    errorMessage = ""
                    viewModel.register(email, password) { uid ->
                        viewModel.saveUserData(uid, name, phoneNumber, bloodType, emergencyContactName1, emergencyContactPhone1, emergencyContactName2, emergencyContactPhone2)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
        )  {
            Text(text = "Registrar", color = Color.White)
        }

        val authResult by viewModel.authResult.collectAsState()
        authResult?.let {
            if (it.isSuccess) {
                navController.navigate("login_screen")
                viewModel.clearAuthResult()
            } else {
                Text("Error al registrar: ${it.exceptionOrNull()?.message}")
                viewModel.clearAuthResult()
            }
        }
    }
}

// InfoScreen: Pantalla de información de la cuenta
@Composable
fun InfoScreen(navController: NavHostController, viewModel: AuthViewModel = viewModel()) {
    val userInfo by viewModel.userInfo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB32222))
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

        // Muestra los datos detalladamente
        userInfo?.let { data ->
            val nombre = data["nombre"] as? String ?: "Desconocido"
            val tipoDeSangre = data["tipoDeSangre"] as? String ?: "No especificado"
            val telefono = data["telefono"] as? String ?: "No especificado"

            Text(
                text = "Nombre: $nombre",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tipo de Sangre: $tipoDeSangre",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Teléfono: $telefono",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } ?: Text(
            text = "Cargando datos...",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("help_screen") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(2.dp, Color.Red, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Regresar", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun EmergencyNumbersScreen(navController: NavHostController, viewModel: AuthViewModel) {
    val userInfo by viewModel.userInfo.collectAsState()

    // Lista de números de emergencia predefinidos
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

        // Título para los números predefinidos
        Text(
            text = "Números de Emergencia ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Mostrar los números de emergencia predefinidos
        emergencyNumbers.forEach { number ->
            Text(
                text = number,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título para los contactos del usuario
        Text(
            text = "Contactos de emergencia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Muestra los contactos de emergencia detalladamente
        userInfo?.let { data ->
            val emergencia1 = data["emergencia1"] as? Map<String, String>
            val emergencia2 = data["emergencia2"] as? Map<String, String>

            emergencia1?.let {
                Text(
                    text = " ${it["nombre"]}",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Teléfono: ${it["numero"]}",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            emergencia2?.let {
                Text(
                    text = " ${it["nombre"]}",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Teléfono: ${it["numero"]}",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        } ?: Text(
            text = "Cargando datos...",
            color = Color.White
        )

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


//HopitalsScreen: pantalla para mostrar los hospitales
@Composable
fun HospitalsScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var hospitalsList by remember { mutableStateOf(listOf<String>()) }
    var connectionError by remember { mutableStateOf(false) }
    var latitude by remember { mutableStateOf("Unknown") }
    var longitude by remember { mutableStateOf("Unknown") }
    val context = LocalContext.current
    val hospitalsApiClient = HospitalsApiClient(context)

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getLocation(fusedLocationClient) { lat, lon ->
                latitude = lat
                longitude = lon
                if (searchQuery.isNotEmpty()) {
                    hospitalsApiClient.fetchNearbyHospitals(lat = latitude.toDouble(), lon = longitude.toDouble()) { jsonResponse ->
                        if (jsonResponse != null) {
                            hospitalsList = processHospitalsResponse(jsonResponse, searchQuery)
                            connectionError = false
                        } else {
                            connectionError = true
                            hospitalsList = listOf()
                        }
                    }
                }
            }
        } else {
            latitude = "Permission Denied"
            longitude = "Permission Denied"
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

            TextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    if (query.isNotEmpty() && latitude != "Unknown" && longitude != "Unknown") {
                        hospitalsApiClient.fetchNearbyHospitals(lat = latitude.toDouble(), lon = longitude.toDouble()) { jsonResponse ->
                            if (jsonResponse != null) {
                                hospitalsList = processHospitalsResponse(jsonResponse, query)
                                connectionError = false
                            } else {
                                connectionError = true
                                hospitalsList = listOf()
                            }
                        }
                    } else {
                        hospitalsList = emptyList()
                    }
                },
                label = { Text("Buscar Hospital", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.7f)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White,
                    textColor = Color.White
                )
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

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                getLocation(fusedLocationClient) { lat, lon ->
                    latitude = lat
                    longitude = lon
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (String, String) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(location.latitude.toString(), location.longitude.toString())
        } else {
            onLocationResult("No location found", "No location found")
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
