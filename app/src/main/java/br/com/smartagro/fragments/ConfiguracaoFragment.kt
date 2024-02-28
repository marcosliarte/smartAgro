package br.com.smartagro.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import br.com.smartagro.LoginActivity
import br.com.smartagro.R
import com.google.firebase.auth.FirebaseAuth

class ConfiguracaoFragment : Fragment() {

    lateinit var txtNome: TextView
    lateinit var txtEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val view = inflater.inflate(R.layout.fragment_configuracao, container, false)


        val botaoSair = view.findViewById<Button>(R.id.btnSair)
        botaoSair?.setOnClickListener {
            sair()
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val usuario = firebaseAuth.currentUser
        txtNome = view.findViewById(R.id.txtNomeCompleto)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtNome.text = usuario?.displayName
        txtEmail.text = usuario?.email

        return view
    }

    private fun sair() {
        FirebaseAuth.getInstance().signOut()
        Intent(context, LoginActivity::class.java).apply {
            startActivity(this)
        }
    }
}