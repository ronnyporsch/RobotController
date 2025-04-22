package de.ronnyporsch.robot_controller.robot_control.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RobotControlScreen(viewModel: RobotControlViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Enter MoveJ Parameters", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))

        uiState.jointValues.forEachIndexed { index, value ->
            TextField(
                value = value,
                onValueChange = { viewModel.updateJointValue(index, it) },
                label = { Text("Joint ${index + 1}") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        TextField(
            value = uiState.acceleration,
            onValueChange = { viewModel.updateAcceleration(it) },
            label = { Text("Acceleration (a)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.velocity,
            onValueChange = { viewModel.updateVelocity(it) },
            label = { Text("Velocity (v)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.submit() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Submit")
            }

            Button(
                onClick = { viewModel.toggleGloveSimulation() },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (uiState.gloveSimulationRunning) "Stop Simulation" else "Start Simulation")
            }
        }
    }

    uiState.errorMessage?.let {
        AlertDialog(
            onDismissRequest = { viewModel.submit() },
            title = { Text("Error") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = { viewModel.submit() }) {
                    Text("OK")
                }
            }
        )
    }
}
