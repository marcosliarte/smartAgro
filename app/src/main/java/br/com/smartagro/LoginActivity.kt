package br.com.smartagro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import br.com.smartagro.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editSenha.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length >= 6) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, PrincipalActivity::class.java)
                            startActivity(intent)
                        } else {
                            val errorText = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Usuário não existe."
                                is FirebaseAuthInvalidCredentialsException -> "Credenciais inválidas."
                                else -> "Erro ao fazer login. Tente novamente mais tarde."
                            }
                            Toast.makeText(this, errorText, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Campos vazios não são permitidos.", Toast.LENGTH_LONG).show()
                validarEmailSenha()
            }
        }

        binding.btnCriarConta.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.btnTenhoConta.setOnClickListener {
            binding.cardLogin.visibility = android.view.View.VISIBLE
        }

        binding.btnClose.setOnClickListener { view ->
            binding.cardLogin.visibility = View.GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        binding.txtRecuperarSenha.setOnClickListener {
            Toast.makeText(this, "Em breve...", Toast.LENGTH_LONG).show() //TODO: Implementar recuperação de senha
        }
    }

    private fun validarEmailSenha(): Boolean {
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        val camposComProblemas = mutableListOf<View>()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Digite um email válido"
            camposComProblemas.add(binding.editEmail)
        }

        if (senha.isEmpty()) {
            binding.editSenha.error = "Campo obrigatório"
            camposComProblemas.add(binding.editSenha)
        }

        if (camposComProblemas.isNotEmpty()) {
            camposComProblemas[0].requestFocus()
            return false
        }

        return true
    }

}