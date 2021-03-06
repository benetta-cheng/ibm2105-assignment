package com.ibm2105.loyaltyapp.prelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ibm2105.loyaltyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private PreLoginViewModel viewModel;
    private TextInputLayout usernameTextInputLayout;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PreLoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextInputLayout = view.findViewById(R.id.textInputUsername);
        TextInputLayout emailTextInputLayout = view.findViewById(R.id.textInputEmail);
        TextInputLayout passwordTextInputLayout = view.findViewById(R.id.textInputPassword);
        TextInputLayout confirmPasswordTextInputLayout = view.findViewById(R.id.textInputConfirmPassword);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null && status.equals(R.string.username_exists)) {
                usernameTextInputLayout.setError(getString(R.string.username_exists));
            }
        });

        Button signUpButton = view.findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInputLayout.getEditText().getText().toString().trim();
                String email = emailTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString();
                String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();

                boolean valid = true;

                if (username.isEmpty()) {
                    usernameTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (usernameTextInputLayout.isErrorEnabled()) {
                    usernameTextInputLayout.setErrorEnabled(false);
                }

                if (email.isEmpty()) {
                    emailTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (emailTextInputLayout.isErrorEnabled()) {
                        emailTextInputLayout.setErrorEnabled(false);
                    }
                } else {
                    emailTextInputLayout.setError(getString(R.string.invalid_email));
                    valid = false;
                }

                if (password.isEmpty()) {
                    passwordTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (passwordTextInputLayout.isErrorEnabled()) {
                    passwordTextInputLayout.setErrorEnabled(false);
                }

                if (confirmPassword.isEmpty()) {
                    confirmPasswordTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (password.equals(confirmPassword)) {
                    if (confirmPasswordTextInputLayout.isErrorEnabled()) {
                        confirmPasswordTextInputLayout.setErrorEnabled(false);
                    }
                } else {
                    confirmPasswordTextInputLayout.setError(getString(R.string.input_mismatch));
                    valid = false;
                }

                if (valid) {
                    viewModel.registerUser(username, email, password);
                }
            }
        });
    }
}