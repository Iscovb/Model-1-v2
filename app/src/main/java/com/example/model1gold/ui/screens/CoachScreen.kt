package com.example.model1gold.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model1gold.model.ChatMessage
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.SlateBorder
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.TextSecondary

private val QUICK_PROMPTS = listOf(
    "Lammaanaha SMT saxda ah?",
    "Saacadaha Purge NY?",
    "Entry #1 Line?",
    "Sharciga 1% Risk & 3R?",
    "Waa maxay KOD?"
)

@Composable
fun CoachScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = if (msg.isUser) GoldPrimary else SlateSurface,
                        border = if (msg.isUser) null else BorderStroke(1.dp, SlateBorder),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (msg.isUser) 16.dp else 4.dp,
                            bottomEnd = if (msg.isUser) 4.dp else 16.dp
                        ),
                        modifier = Modifier.fillMaxWidth(0.88f)
                    ) {
                        Text(
                            text = msg.content,
                            color = if (msg.isUser) Color.Black else MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = if (msg.isUser) FontWeight.Medium else FontWeight.Normal,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        // Quick prompts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QUICK_PROMPTS.forEach { prompt ->
                AssistChip(
                    onClick = { onSendMessage(prompt) },
                    label = { Text(prompt, fontSize = 11.sp) }
                )
            }
        }

        // Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Weydii sharci kasta oo Model#1 ah...", fontSize = 13.sp, color = TextSecondary) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("coach_input_field"),
                maxLines = 3,
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onSendMessage(input)
                        input = ""
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color.Black
                ),
                modifier = Modifier.testTag("coach_send_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
            }
        }
    }
}
