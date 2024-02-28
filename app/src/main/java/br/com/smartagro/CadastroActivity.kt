package br.com.smartagro

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.smartagro.clima.Cidade
import br.com.smartagro.clima.Conexao
import br.com.smartagro.clima.ConsumirXML
import br.com.smartagro.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Normalizer
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    var cidadeList: List<Cidade> = ArrayList()
    private var cidadeSelecionada = Cidade()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnCriarConta.setOnClickListener {
            if (validarCampos()) {
                salvar()
            }else{
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imgBuscarCidade.setOnClickListener {
            definirCidade()
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val editCelular = findViewById<EditText>(R.id.editCelular)
        editCelular.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))

    }
    private fun validarCampos(): Boolean {
        val nomeCompleto = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()
        val celular = binding.editCelular.text.toString().replace(" ", "").replace("-", "")
        val cidade = binding.editCidade.text.toString()

        val camposComProblemas = mutableListOf<View>()

        if (nomeCompleto.isEmpty()) {
            binding.editNome.error = "Campo obrigatório"
            camposComProblemas.add(binding.editNome)
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Digite um email válido"
            camposComProblemas.add(binding.editEmail)
        }

        if (senha.isEmpty()) {
            binding.editSenha.error = "Campo obrigatório"
            camposComProblemas.add(binding.editSenha)
        }

        if (senha.length < 6) {
            binding.editSenha.error = "A senha deve ter pelo menos 6 caracteres"
            camposComProblemas.add(binding.editSenha)
        }


        if (celular.length < 10 || celular.length > 11) {
            binding.editCelular.error = "Digite um número de celular válido no formato 99 99999-9999"
            camposComProblemas.add(binding.editCelular)
        }

        if (celular.isEmpty()) {
            binding.editCelular.error = "Campo obrigatório"
            camposComProblemas.add(binding.editCelular)
        }

        if (cidadeSelecionada.getId().isEmpty()) {
            binding.editCidade.error = "Clique na lupa para pesquisar uma cidade válida"
            camposComProblemas.add(binding.editCidade)
        }

        if (cidade.isEmpty()) {
            binding.editCidade.error = "Campo obrigatório"
            camposComProblemas.add(binding.editCidade)
        }

        if (camposComProblemas.isNotEmpty()) {
            camposComProblemas[0].requestFocus()
            return false
        }

        return true
    }

    private fun salvar() {
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()
        val nomeCompleto = binding.editNome.text.toString()
        val cidade = binding.editCidade.text.toString()
        val celular = binding.editCelular.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nomeCompleto)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Salve os campos adicionais no Firestore associados ao UID do usuário
                                saveUserDataToFirestore(user?.uid, nomeCompleto, cidade, celular)

                                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                            } else {
                                Toast.makeText(this, "Erro ao atualizar informações do usuário.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                }
            }
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun saveUserDataToFirestore(userId: String?, nomeCompleto: String, cidade: String, celular: String) {
        if (userId != null) {
            val userData = hashMapOf(
                "nomeCompleto" to nomeCompleto,
                "celular" to celular.replace(" ", "").replace("-", "")
            )
            salvarCidadeSelecionadaNoFirestore(cidadeSelecionada)

            firestore.collection("usuarios")
                .document(userId)
                .set(userData)
                .addOnSuccessListener {
                    // Campos adicionais salvos com sucesso no Firestore
                }
                .addOnFailureListener { e ->
                    // Erro ao salvar os campos adicionais no Firestore
                    Toast.makeText(this, "Erro ao salvar os campos adicionais do usuário.", Toast.LENGTH_SHORT).show()
                }
        } else {
            // UID do usuário não é válido, trate esse caso de erro adequadamente
        }
    }

    private fun salvarCidadeSelecionadaNoFirestore(cidade: Cidade) {

        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val cidadeData = hashMapOf(
                "id" to cidade.getId(),
                "nome" to cidade.getNome(),
                "uf" to cidade.getUf(),
            )

            firestore.collection("usuarios")
                .document(userId)
                .collection("cidade")
                .document(cidade.getId())
                .set(cidadeData)
                .addOnFailureListener { e ->
                    println( "Erro ao salvar cidade selecionada no Firestore.")
                    Log.e("FirestoreError", e.message ?: "")
                }
        }
    }

    fun removerAcentosEAjustaUrl(texto: String?): String? {
        var textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
        textoNormalizado = textoNormalizado.replace("[^\\p{ASCII}]".toRegex(), "")
        textoNormalizado = textoNormalizado.replace(" ", "%20")
        return textoNormalizado
    }


    private fun definirCidade() {
        val cidade = binding.editCidade.text.toString()
        try {
            val url = "http://servicos.cptec.inpe.br/XML/listaCidades?city=" + removerAcentosEAjustaUrl(cidade)
            Tarefa(this).execute(url)
        } catch (e: Exception) {
            Log.e("Erro", e.message!!)
        }
    }

    inner class Tarefa(private val context: Context) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg strings: String?): String {
            val dados = Conexao.getDados(strings[0])
            Log.d("Dados", "Dados: $dados")
            return dados
        }

        override fun onPostExecute(s: String?) {
            cidadeList = ConsumirXML.getCidade(s)

            exibirPopupSelecaoCidade(cidadeList)

            if (cidadeList.isEmpty()) {
                val cidade = findViewById<EditText>(R.id.editCidade)
                Toast.makeText(context, "Cidade não encontrada!", Toast.LENGTH_SHORT).show()
                cidade.setText("")
            }
        }

        private fun exibirPopupSelecaoCidade(cidades: List<Cidade>) {
            val nomesCidades = Array<String>(cidades.size) { "" }
            for (i in cidades.indices) {
                nomesCidades[i] = "${cidades[i].getNome()} - ${cidades[i].getUf()}"
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Selecione a cidade")
            builder.setItems(nomesCidades) { dialog, which ->
                cidadeSelecionada = cidades[which]
                binding.editCidade.setText(cidadeSelecionada.getNome() + " - " + cidadeSelecionada.getUf())
                binding.editCidade.error = null

            }
            builder.show()
        }

    }
}
